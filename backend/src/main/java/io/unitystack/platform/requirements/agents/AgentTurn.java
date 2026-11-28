package io.unitystack.platform.requirements.agents;

import io.unitystack.platform.requirements.domain.Charter;
import java.util.List;

/** What the inception agent proposes after one user turn. Untrusted until validated by InceptionService. */
public record AgentTurn(String reply, Charter charter, List<String> openQuestions) {}
