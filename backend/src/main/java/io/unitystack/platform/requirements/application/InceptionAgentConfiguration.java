package io.unitystack.platform.requirements.application;

import io.unitystack.platform.core.ai.LlmClient;
import io.unitystack.platform.requirements.agents.InceptionAgent;
import io.unitystack.platform.requirements.agents.LlmInceptionAgent;
import io.unitystack.platform.requirements.agents.MockInceptionAgent;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

/**
 * Wires the framework-free inception agent. Core registers an {@link LlmClient} only for a real provider;
 * with {@code unitystack.ai.provider=mock} there is none, and the deterministic scripted agent is used.
 */
@Configuration(proxyBeanMethods = false)
class InceptionAgentConfiguration {

    @Bean
    InceptionAgent inceptionAgent(ObjectProvider<LlmClient> llm, ObjectMapper json) {
        LlmClient client = llm.getIfAvailable();
        return client == null ? new MockInceptionAgent() : new LlmInceptionAgent(client, json);
    }
}
