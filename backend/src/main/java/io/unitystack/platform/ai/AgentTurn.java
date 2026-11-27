package io.unitystack.platform.ai;

import io.unitystack.platform.inception.Charter;
import java.util.List;

/** What the inception agent proposes after one user turn. Untrusted until validated by InceptionService. */
public record AgentTurn(String reply, Charter charter, List<String> openQuestions) {
}
