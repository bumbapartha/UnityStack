package io.unitystack.platform.core.ai;

/**
 * Provider-neutral access to a large language model (AD-1, FR-026). Feature-module agents depend only on
 * this interface; provider implementations live in sub-packages and are chosen by
 * {@code unitystack.ai.provider}. Plain Java, so the AI layer can move to another language later.
 */
public interface LlmClient {

    /**
     * Sends one chat completion request and returns the model's text output. When the request has a
     * response schema, the output is JSON that the caller must still validate (it is untrusted).
     *
     * @throws AiUnavailableException when the provider fails, refuses or returns nothing usable
     */
    String complete(LlmRequest request);

    /** Identifies provider and model for audit records, e.g. "openai:gpt-4.1-mini". */
    String ref();
}
