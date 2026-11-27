package io.unitystack.platform.ai;

import io.unitystack.platform.common.ApiException;
import org.springframework.http.HttpStatus;

public class AiUnavailableException extends ApiException {
    public AiUnavailableException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}
