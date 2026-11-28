package io.unitystack.platform.requirements.application;

import io.unitystack.platform.core.common.ApiException;
import io.unitystack.platform.requirements.agents.AgentTurn;
import io.unitystack.platform.requirements.agents.InceptionAgent;
import io.unitystack.platform.requirements.api.InceptionTurnRequest;
import io.unitystack.platform.requirements.api.InceptionTurnResponse;
import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Runs one inception turn. Stateless: nothing is persisted until the user confirms creation, so an
 * abandoned conversation is simply discarded (FR-105).
 */
@Service
public class InceptionService {
    static final int MAX_MESSAGE_CHARS = 4000;
    static final int MAX_HISTORY = 80;
    static final int MAX_OPEN_QUESTIONS = 20;

    private final InceptionAgent agent;

    public InceptionService(InceptionAgent agent) {
        this.agent = agent;
    }

    public InceptionTurnResponse turn(InceptionTurnRequest req) {
        List<ChatMessage> history = validHistory(req.history());
        Charter charter = (req.charter() == null ? Charter.empty() : req.charter()).normalised();
        List<String> questions = cleanQuestions(req.openQuestions());

        String message = req.message() == null ? null : req.message().strip();
        if (message != null && !message.isEmpty()) {
            if (message.length() > MAX_MESSAGE_CHARS) {
                throw new ApiException(
                        HttpStatus.BAD_REQUEST, "Message is too long (max " + MAX_MESSAGE_CHARS + " characters)");
            }
            history.add(new ChatMessage("user", message));
        } else if (!history.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Message must not be empty");
        }
        if (history.size() > MAX_HISTORY) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST, "Conversation is too long; please create the project or start again");
        }

        AgentTurn turn = agent.respond(List.copyOf(history), charter, questions);

        // The agent's output is untrusted: normalise and recompute readiness deterministically.
        Charter updated = (turn.charter() == null ? charter : turn.charter()).normalised();
        List<String> updatedQuestions = cleanQuestions(turn.openQuestions() == null ? questions : turn.openQuestions());
        String reply = turn.reply() == null || turn.reply().isBlank()
                ? "Could you tell me a bit more?"
                : turn.reply().strip();
        history.add(new ChatMessage("assistant", reply));

        return new InceptionTurnResponse(
                history, updated, updatedQuestions, updated.isReady(), updated.missingMandatory(), agent.agentRef());
    }

    static List<ChatMessage> validHistory(List<ChatMessage> history) {
        List<ChatMessage> out = new ArrayList<>();
        if (history == null) return out;
        for (ChatMessage m : history) {
            if (m == null || m.content() == null || !("user".equals(m.role()) || "assistant".equals(m.role()))) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid conversation history");
            }
            if (m.content().length() > MAX_MESSAGE_CHARS * 2) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Conversation history contains an oversized message");
            }
            out.add(m);
        }
        return out;
    }

    static List<String> cleanQuestions(List<String> questions) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        if (questions != null) {
            for (String q : questions) {
                String c = Charter.clean(q);
                if (c != null) set.add(c.length() > 500 ? c.substring(0, 500) : c);
            }
        }
        return new ArrayList<>(set).subList(0, Math.min(set.size(), MAX_OPEN_QUESTIONS));
    }
}
