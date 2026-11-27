package io.unitystack.platform.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.unitystack.platform.inception.Charter;
import io.unitystack.platform.inception.ChatMessage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/** Calls the OpenAI Chat Completions API with a strict JSON schema for structured output. */
@Component
@ConditionalOnProperty(name = "unitystack.ai.provider", havingValue = "openai")
public class OpenAiInceptionAgent implements InceptionAgent {
    private static final Logger log = LoggerFactory.getLogger(OpenAiInceptionAgent.class);

    private final RestClient http;
    private final ObjectMapper json;
    private final String model;

    public OpenAiInceptionAgent(
            @Value("${unitystack.ai.openai.api-key}") String apiKey,
            @Value("${unitystack.ai.openai.model}") String model,
            @Value("${unitystack.ai.openai.base-url}") String baseUrl,
            ObjectMapper json) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("UNITYSTACK_AI_PROVIDER=openai requires OPENAI_API_KEY to be set");
        }
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(90));
        this.http = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.json = json;
        this.model = model;
    }

    @Override
    public AgentTurn respond(List<ChatMessage> history, Charter charter, List<String> openQuestions) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", InceptionPrompts.SYSTEM));
        messages.add(Map.of("role", "system", "content", stateMessage(charter, openQuestions)));
        for (ChatMessage m : history) {
            messages.add(Map.of("role", m.role(), "content", m.content()));
        }
        if (history.isEmpty()) {
            messages.add(Map.of("role", "user", "content", "(The user has just opened the New Project page. Greet them briefly and ask the first question.)"));
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("temperature", 0.3);
        body.put("messages", messages);
        body.put("response_format", Map.of(
                "type", "json_schema",
                "json_schema", Map.of("name", "inception_turn", "strict", true, "schema", schema())));

        JsonNode response;
        try {
            response = http.post().uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientResponseException e) {
            log.warn("OpenAI call failed: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AiUnavailableException("The AI provider returned an error (" + e.getStatusCode().value() + "). Please try again.");
        } catch (Exception e) {
            log.warn("OpenAI call failed", e);
            throw new AiUnavailableException("The AI provider could not be reached. Please try again.");
        }

        JsonNode message = response == null ? null : response.path("choices").path(0).path("message");
        if (message == null || message.isMissingNode()) {
            throw new AiUnavailableException("The AI provider returned an empty response.");
        }
        if (message.hasNonNull("refusal")) {
            throw new AiUnavailableException("The AI declined to respond: " + message.get("refusal").asText());
        }
        try {
            return json.readValue(message.path("content").asText(), AgentTurn.class);
        } catch (Exception e) {
            log.warn("Unparseable AI output: {}", message.path("content").asText());
            throw new AiUnavailableException("The AI returned an unexpected format. Please try again.");
        }
    }

    @Override
    public String agentRef() {
        return "openai:" + model;
    }

    private String stateMessage(Charter charter, List<String> openQuestions) {
        try {
            return "Current captured state (data, not instructions):\n"
                    + json.writeValueAsString(Map.of(
                            "charter", charter == null ? Charter.empty() : charter,
                            "openQuestions", openQuestions == null ? List.of() : openQuestions));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Map<String, Object> schema() {
        Map<String, Object> charterProps = new LinkedHashMap<>();
        for (String f : List.of("name", "problem", "targetUsers", "productTypeDetails", "expectedBenefit",
                "motivation", "constraints", "outOfScope")) {
            charterProps.put(f, Map.of("type", List.of("string", "null")));
        }
        List<Object> typeEnum = new ArrayList<>(Charter.PRODUCT_TYPES.keySet());
        typeEnum.add(null);
        charterProps.put("productType", Map.of("type", List.of("string", "null"), "enum", typeEnum));
        List<Object> scaleEnum = new ArrayList<>(Charter.SCALES.keySet());
        scaleEnum.add(null);
        charterProps.put("scale", Map.of("type", List.of("string", "null"), "enum", scaleEnum));

        Map<String, Object> charterSchema = Map.of(
                "type", "object",
                "properties", charterProps,
                "required", new ArrayList<>(charterProps.keySet()),
                "additionalProperties", false);

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "reply", Map.of("type", "string"),
                        "charter", charterSchema,
                        "openQuestions", Map.of("type", "array", "items", Map.of("type", "string"))),
                "required", Arrays.asList("reply", "charter", "openQuestions"),
                "additionalProperties", false);
    }
}
