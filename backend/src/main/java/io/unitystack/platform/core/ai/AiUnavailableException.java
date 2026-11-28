package io.unitystack.platform.core.ai;

/**
 * The AI provider failed, refused, or returned output that could not be used. The message is safe to show
 * to the user. Mapped to HTTP 502 by the shared API error handler; kept free of web types (AD-1).
 */
public class AiUnavailableException extends RuntimeException {
    public AiUnavailableException(String message) {
        super(message);
    }
}
