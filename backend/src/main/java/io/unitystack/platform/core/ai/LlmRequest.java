package io.unitystack.platform.core.ai;

import java.util.List;
import java.util.Map;

/**
 * A chat completion request.
 *
 * @param messages       the conversation, in order
 * @param temperature    sampling temperature
 * @param responseSchema when not null, the provider must return JSON matching this schema (strict mode)
 */
public record LlmRequest(List<LlmMessage> messages, double temperature, JsonSchema responseSchema) {

    public LlmRequest {
        messages = List.copyOf(messages);
    }

    /** A named JSON schema for structured output. */
    public record JsonSchema(String name, Map<String, Object> schema) {
    }
}
