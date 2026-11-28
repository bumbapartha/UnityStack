package io.unitystack.platform.requirements.agents;

import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.List;

/** Provider-neutral boundary for the inception AI (FR-026). */
public interface InceptionAgent {

    /**
     * @param history       conversation so far; the latest user message (if any) is last
     * @param charter       charter as currently captured
     * @param openQuestions unknowns recorded so far
     */
    AgentTurn respond(List<ChatMessage> history, Charter charter, List<String> openQuestions);

    /** Identifies provider and model for audit records, e.g. "openai:gpt-4.1-mini". */
    String agentRef();
}
