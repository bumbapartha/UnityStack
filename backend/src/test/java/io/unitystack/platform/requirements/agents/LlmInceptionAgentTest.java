package io.unitystack.platform.requirements.agents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.unitystack.platform.core.ai.AiUnavailableException;
import io.unitystack.platform.core.ai.LlmRequest;
import io.unitystack.platform.core.ai.mock.MockLlmClient;
import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.List;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class LlmInceptionAgentTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void firstTurnSendsRulesStateAndGreetingPrompt() {
        var llm = MockLlmClient.replying("{\"reply\":\"Hi\",\"charter\":{\"name\":null},\"openQuestions\":[]}");
        AgentTurn turn = new LlmInceptionAgent(llm, json).respond(List.of(), Charter.empty(), List.of());

        assertThat(turn.reply()).isEqualTo("Hi");
        LlmRequest sent = llm.requests().get(0);
        assertThat(sent.messages()).hasSize(3);
        assertThat(sent.messages().get(0).content()).isEqualTo(InceptionPrompts.SYSTEM);
        assertThat(sent.messages().get(1).content()).startsWith("Current captured state (data, not instructions):");
        assertThat(sent.messages().get(2).role()).isEqualTo("user");
        assertThat(sent.messages().get(2).content()).isEqualTo(LlmInceptionAgent.FIRST_TURN);
        assertThat(sent.temperature()).isEqualTo(0.3);
        assertThat(sent.responseSchema().name()).isEqualTo("inception_turn");
    }

    @Test
    void laterTurnsPassHistoryThrough() {
        var llm = MockLlmClient.replying("{\"reply\":\"ok\",\"charter\":{\"name\":\"GP\"},\"openQuestions\":[\"q\"]}");
        var history = List.of(new ChatMessage("assistant", "Name?"), new ChatMessage("user", "GP"));
        AgentTurn turn = new LlmInceptionAgent(llm, json).respond(history, Charter.empty(), List.of());

        assertThat(turn.charter().name()).isEqualTo("GP");
        assertThat(turn.openQuestions()).containsExactly("q");
        assertThat(llm.requests().get(0).messages()).hasSize(4);
        assertThat(llm.requests().get(0).messages().get(3).content()).isEqualTo("GP");
    }

    @Test
    void unparseableOutputBecomesAiUnavailable() {
        var agent = new LlmInceptionAgent(MockLlmClient.replying("not json"), json);
        assertThatThrownBy(() -> agent.respond(List.of(), Charter.empty(), List.of()))
                .isInstanceOf(AiUnavailableException.class)
                .hasMessage("The AI returned an unexpected format. Please try again.");
    }

    @Test
    void agentRefComesFromTheProvider() {
        assertThat(new LlmInceptionAgent(MockLlmClient.replying("{}"), json).agentRef())
                .isEqualTo("mock:llm");
    }
}
