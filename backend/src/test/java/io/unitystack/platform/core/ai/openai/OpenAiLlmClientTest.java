package io.unitystack.platform.core.ai.openai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.net.httpserver.HttpServer;
import io.unitystack.platform.core.ai.AiUnavailableException;
import io.unitystack.platform.core.ai.LlmMessage;
import io.unitystack.platform.core.ai.LlmRequest;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/** Runs against a throwaway server on localhost; no real provider is called. */
class OpenAiLlmClientTest {
    private final ObjectMapper json = new ObjectMapper();
    private final AtomicReference<String> lastBody = new AtomicReference<>();
    private final AtomicReference<String> lastAuth = new AtomicReference<>();
    private final AtomicReference<String> lastPath = new AtomicReference<>();
    private HttpServer server;

    private String serve(int status, String body) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            lastPath.set(exchange.getRequestURI().getPath());
            lastAuth.set(exchange.getRequestHeaders().getFirst("Authorization"));
            lastBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/v1";
    }

    @AfterEach
    void stop() {
        if (server != null) server.stop(0);
    }

    private static LlmRequest request() {
        return new LlmRequest(
                List.of(LlmMessage.system("rules"), LlmMessage.user("hi")),
                0.3,
                new LlmRequest.JsonSchema("inception_turn", Map.of("type", "object")));
    }

    @Test
    void sendsStructuredOutputRequestAndReturnsContent() throws Exception {
        String base = serve(200, "{\"choices\":[{\"message\":{\"content\":\"{\\\"reply\\\":\\\"ok\\\"}\"}}]}");
        var client = new OpenAiLlmClient("sk-test", "gpt-test", base, json);

        assertThat(client.complete(request())).isEqualTo("{\"reply\":\"ok\"}");
        assertThat(lastPath.get()).isEqualTo("/v1/chat/completions");
        assertThat(lastAuth.get()).isEqualTo("Bearer sk-test");
        JsonNode body = json.readTree(lastBody.get());
        assertThat(body.path("model").asString()).isEqualTo("gpt-test");
        assertThat(body.path("temperature").asDouble()).isEqualTo(0.3);
        assertThat(body.path("messages").get(1).path("role").asString()).isEqualTo("user");
        assertThat(body.path("response_format").path("type").asString()).isEqualTo("json_schema");
        assertThat(body.path("response_format").path("json_schema").path("name").asString())
                .isEqualTo("inception_turn");
        assertThat(body.path("response_format")
                        .path("json_schema")
                        .path("strict")
                        .asBoolean())
                .isTrue();
        assertThat(client.ref()).isEqualTo("openai:gpt-test");
    }

    @Test
    void providerErrorBecomesAiUnavailableWithStatus() throws Exception {
        var client = new OpenAiLlmClient("sk-test", "m", serve(429, "{\"error\":\"rate\"}"), json);
        assertThatThrownBy(() -> client.complete(request()))
                .isInstanceOf(AiUnavailableException.class)
                .hasMessage("The AI provider returned an error (429). Please try again.");
    }

    @Test
    void refusalIsReported() throws Exception {
        var client = new OpenAiLlmClient(
                "sk-test", "m", serve(200, "{\"choices\":[{\"message\":{\"refusal\":\"no\"}}]}"), json);
        assertThatThrownBy(() -> client.complete(request())).hasMessage("The AI declined to respond: no");
    }

    @Test
    void emptyResponseIsReported() throws Exception {
        var client = new OpenAiLlmClient("sk-test", "m", serve(200, "{\"choices\":[]}"), json);
        assertThatThrownBy(() -> client.complete(request())).hasMessage("The AI provider returned an empty response.");
    }

    @Test
    void unreachableProviderIsReported() {
        var client = new OpenAiLlmClient("sk-test", "m", "http://127.0.0.1:1/v1", json);
        assertThatThrownBy(() -> client.complete(request()))
                .isInstanceOf(AiUnavailableException.class)
                .hasMessage("The AI provider could not be reached. Please try again.");
    }

    @Test
    void requiresApiKey() {
        assertThatThrownBy(() -> new OpenAiLlmClient(" ", "m", "http://x", json))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("OPENAI_API_KEY");
    }
}
