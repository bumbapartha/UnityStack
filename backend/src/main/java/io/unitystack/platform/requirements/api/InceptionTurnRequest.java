package io.unitystack.platform.requirements.api;

import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.List;

/** One inception turn. The client holds the state; nothing is stored until creation (FR-105). */
public record InceptionTurnRequest(
        List<ChatMessage> history, Charter charter, List<String> openQuestions, String message) {}
