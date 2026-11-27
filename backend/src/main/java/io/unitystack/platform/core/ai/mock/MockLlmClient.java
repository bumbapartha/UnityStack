package io.unitystack.platform.core.ai.mock;

import io.unitystack.platform.core.ai.LlmClient;
import io.unitystack.platform.core.ai.LlmRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * In-memory LLM for tests and offline development: answers with a caller-supplied function and records
 * every request so tests can inspect what an agent sent. Makes no network calls.
 */
public final class MockLlmClient implements LlmClient {
    private final Function<LlmRequest, String> responder;
    private final List<LlmRequest> requests = new ArrayList<>();

    public MockLlmClient(Function<LlmRequest, String> responder) {
        this.responder = responder;
    }

    /** Always answers with the same text. */
    public static MockLlmClient replying(String output) {
        return new MockLlmClient(request -> output);
    }

    @Override
    public String complete(LlmRequest request) {
        requests.add(request);
        return responder.apply(request);
    }

    @Override
    public String ref() {
        return "mock:llm";
    }

    public List<LlmRequest> requests() {
        return List.copyOf(requests);
    }
}
