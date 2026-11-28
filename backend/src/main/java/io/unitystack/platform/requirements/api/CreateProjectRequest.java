package io.unitystack.platform.requirements.api;

import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.List;

public record CreateProjectRequest(Charter charter, List<String> openQuestions, List<ChatMessage> transcript) {
}
