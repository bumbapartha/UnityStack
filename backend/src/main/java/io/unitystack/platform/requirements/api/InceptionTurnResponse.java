package io.unitystack.platform.requirements.api;

import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.List;

public record InceptionTurnResponse(List<ChatMessage> history, Charter charter, List<String> openQuestions,
                                    boolean ready, List<String> missing, String agentRef) {
}
