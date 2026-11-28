package io.unitystack.platform.core.ai.openai;

import io.unitystack.platform.core.ai.AiUnavailableException;
import io.unitystack.platform.core.ai.LlmClient;
import io.unitystack.platform.core.ai.LlmMessage;
import io.unitystack.platform.core.ai.LlmRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Calls the OpenAI Chat Completions API, using a strict JSON schema for structured output when the request
 * has one. Plain Java (JDK HTTP client and Jackson) so the AI layer stays framework-free (AD-1).
 */
public final class OpenAiLlmClient implements LlmClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiLlmClient.class);
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(90);

    private final HttpClient http;
    private final URI endpoint;
    private final String apiKey;
    private final String model;
    private final ObjectMapper json;

    public OpenAiLlmClient(String apiKey, String model, String baseUrl, ObjectMapper json) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("UNITYSTACK_AI_PROVIDER=openai requires OPENAI_API_KEY to be set");
        }
        this.http = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
        String base = baseUrl;
        while (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        this.endpoint = URI.create(base + "/chat/completions");
        this.apiKey = apiKey;
        this.model = model;
        this.json = json;
    }

    @Override
    public String complete(LlmRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();
        for (LlmMessage m : request.messages()) {
            messages.add(Map.of("role", m.role(), "content", m.content()));
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("temperature", request.temperature());
        body.put("messages", messages);
        if (request.responseSchema() != null) {
            body.put(
                    "response_format",
                    Map.of(
                            "type",
                            "json_schema",
                            "json_schema",
                            Map.of(
                                    "name", request.responseSchema().name(),
                                    "strict", true,
                                    "schema", request.responseSchema().schema())));
        }

        JsonNode response;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(endpoint)
                    .timeout(READ_TIMEOUT)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(body)))
                    .build();
            HttpResponse<String> httpResponse = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            int status = httpResponse.statusCode();
            if (status < 200 || status >= 300) {
                log.warn("OpenAI call failed: {} {}", status, httpResponse.body());
                throw new AiUnavailableException(
                        "The AI provider returned an error (" + status + "). Please try again.");
            }
            response = json.readTree(httpResponse.body());
        } catch (AiUnavailableException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("OpenAI call interrupted", e);
            throw new AiUnavailableException("The AI provider could not be reached. Please try again.");
        } catch (IOException | RuntimeException e) {
            log.warn("OpenAI call failed", e);
            throw new AiUnavailableException("The AI provider could not be reached. Please try again.");
        }

        JsonNode message =
                response == null ? null : response.path("choices").path(0).path("message");
        if (message == null || message.isMissingNode()) {
            throw new AiUnavailableException("The AI provider returned an empty response.");
        }
        if (message.hasNonNull("refusal")) {
            throw new AiUnavailableException(
                    "The AI declined to respond: " + message.get("refusal").asString());
        }
        return message.path("content").asString();
    }

    @Override
    public String ref() {
        return "openai:" + model;
    }
}
