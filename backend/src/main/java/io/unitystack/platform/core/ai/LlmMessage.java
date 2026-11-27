package io.unitystack.platform.core.ai;

/** One chat message sent to a model. role is "system", "user" or "assistant". */
public record LlmMessage(String role, String content) {

    public static LlmMessage system(String content) {
        return new LlmMessage("system", content);
    }

    public static LlmMessage user(String content) {
        return new LlmMessage("user", content);
    }
}
