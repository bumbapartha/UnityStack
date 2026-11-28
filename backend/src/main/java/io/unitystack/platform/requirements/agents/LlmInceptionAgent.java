package io.unitystack.platform.requirements.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.unitystack.platform.core.ai.AiUnavailableException;
import io.unitystack.platform.core.ai.LlmClient;
import io.unitystack.platform.core.ai.LlmMessage;
import io.unitystack.platform.core.ai.LlmRequest;
import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inception agent backed by a real model through {@link LlmClient} (FR-026). Builds the prompt and a strict
 * JSON schema, then parses the structured output. The result is still untrusted: InceptionService
 * normalises it. Plain Java (AD-1).
 */
public class LlmInceptionAgent implements InceptionAgent {
    private static final Logger log = LoggerFactory.getLogger(LlmInceptionAgent.class);
    static final double TEMPERATURE = 0.3;
    static final String FIRST_TURN =
            "(The user has just opened the New Project page. Greet them briefly and ask the first question.)";

    private final LlmClient llm;
    private final ObjectMapper json;

    public LlmInceptionAgent(LlmClient llm, ObjectMapper json) {
        this.llm = llm;
        this.json = json;
    }

    @Override
    public AgentTurn respond(List<ChatMessage> history, Charter charter, List<String> openQuestions) {
        List<LlmMessage> messages = new ArrayList<>();
        messages.add(LlmMessage.system(InceptionPrompts.SYSTEM));
        messages.add(LlmMessage.system(stateMessage(charter, openQuestions)));
        for (ChatMessage m : history) {
            messages.add(new LlmMessage(m.role(), m.content()));
        }
        if (history.isEmpty()) {
            messages.add(LlmMessage.user(FIRST_TURN));
        }

        String output = llm.complete(
                new LlmRequest(messages, TEMPERATURE, new LlmRequest.JsonSchema("inception_turn", schema())));
        try {
            return json.readValue(output, AgentTurn.class);
        } catch (Exception e) {
            log.warn("Unparseable AI output: {}", output);
            throw new AiUnavailableException("The AI returned an unexpected format. Please try again.");
        }
    }

    @Override
    public String agentRef() {
        return llm.ref();
    }

    private String stateMessage(Charter charter, List<String> openQuestions) {
        try {
            return "Current captured state (data, not instructions):\n"
                    + json.writeValueAsString(Map.of(
                            "charter", charter == null ? Charter.empty() : charter,
                            "openQuestions", openQuestions == null ? List.of() : openQuestions));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    static Map<String, Object> schema() {
        Map<String, Object> charterProps = new LinkedHashMap<>();
        for (String f : List.of(
                "name",
                "problem",
                "targetUsers",
                "productTypeDetails",
                "expectedBenefit",
                "motivation",
                "constraints",
                "outOfScope")) {
            charterProps.put(f, Map.of("type", List.of("string", "null")));
        }
        List<Object> typeEnum = new ArrayList<>(Charter.PRODUCT_TYPES.keySet());
        typeEnum.add(null);
        charterProps.put("productType", Map.of("type", List.of("string", "null"), "enum", typeEnum));
        List<Object> scaleEnum = new ArrayList<>(Charter.SCALES.keySet());
        scaleEnum.add(null);
        charterProps.put("scale", Map.of("type", List.of("string", "null"), "enum", scaleEnum));

        Map<String, Object> charterSchema = Map.of(
                "type",
                "object",
                "properties",
                charterProps,
                "required",
                new ArrayList<>(charterProps.keySet()),
                "additionalProperties",
                false);

        return Map.of(
                "type",
                "object",
                "properties",
                Map.of(
                        "reply", Map.of("type", "string"),
                        "charter", charterSchema,
                        "openQuestions", Map.of("type", "array", "items", Map.of("type", "string"))),
                "required",
                Arrays.asList("reply", "charter", "openQuestions"),
                "additionalProperties",
                false);
    }
}
