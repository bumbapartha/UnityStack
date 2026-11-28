package io.unitystack.platform.core.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.unitystack.platform.core.ai.LlmClient;
import io.unitystack.platform.core.ai.openai.OpenAiLlmClient;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the framework-free AI layer. {@code unitystack.ai.provider} selects the provider:
 * {@code openai} registers an {@link LlmClient}; {@code mock} (the default) registers none, and agents use
 * their own deterministic scripted implementation instead, so no model is called.
 */
@Configuration(proxyBeanMethods = false)
class AiConfiguration {
    static final Set<String> PROVIDERS = Set.of("mock", "openai");

    AiConfiguration(@Value("${unitystack.ai.provider:mock}") String provider) {
        if (!PROVIDERS.contains(provider)) {
            throw new IllegalStateException("Unknown unitystack.ai.provider '" + provider + "'; expected one of " + PROVIDERS);
        }
    }

    @Bean
    @ConditionalOnProperty(name = "unitystack.ai.provider", havingValue = "openai")
    LlmClient openAiLlmClient(
            @Value("${unitystack.ai.openai.api-key}") String apiKey,
            @Value("${unitystack.ai.openai.model}") String model,
            @Value("${unitystack.ai.openai.base-url}") String baseUrl,
            ObjectMapper json) {
        return new OpenAiLlmClient(apiKey, model, baseUrl, json);
    }
}
