package io.unitystack.platform.requirements.agents;

import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Deterministic scripted agent for local development and tests; asks each charter item in order.
 * Used when unitystack.ai.provider=mock (the default), so no LLM is called. Plain Java (AD-1).
 */
public class MockInceptionAgent implements InceptionAgent {

    static final String NOT_SPECIFIED = "Not specified";

    private static final Map<String, String> QUESTIONS = Map.of(
            "name", "Hi! Let's set up your new project. What should we call it?",
            "problem", "What problem will this project solve, and for whom?",
            "targetUsers", "Who will use it?",
            "productType",
                    "What kind of product is it: a website, mobile app, API/backend, desktop app, internal tool, data/AI product, or something else?",
            "expectedBenefit", "Once this exists, what will be better for its users or the business?",
            "scale",
                    "How big is this: a personal or team tool, one department, the whole organisation, or public (external customers)?",
            "motivation", "Optional: why is this needed now? (say \"skip\" to move on)",
            "constraints",
                    "Optional: any constraints, such as a deadline, budget, compliance or technology preferences? (or \"skip\")",
            "outOfScope", "Optional: anything this project should definitely NOT do? (or \"skip\")");

    private static final Map<String, String> LABELS = Map.of(
            "targetUsers", "target users",
            "productType", "product type",
            "expectedBenefit", "expected benefit",
            "outOfScope", "out of scope");

    @Override
    public AgentTurn respond(List<ChatMessage> history, Charter charter, List<String> openQuestions) {
        Charter c = charter == null ? Charter.empty() : charter;
        List<String> questions = new ArrayList<>(openQuestions == null ? List.of() : openQuestions);
        String userText = lastUserMessage(history);

        String asked = nextField(c);
        String prefix = "";
        if (userText != null && asked != null) {
            boolean unknown = isUnknown(userText);
            boolean skip = isSkip(userText);
            boolean mandatory = Charter.MANDATORY.contains(asked);
            String openQuestion = "Unanswered: " + LABELS.getOrDefault(asked, asked);
            if (unknown && !questions.contains(openQuestion)) {
                questions.add(openQuestion);
            }
            if (mandatory && (unknown || skip)) {
                return new AgentTurn(
                        "That one is needed to create the project. What's your best current guess? "
                                + QUESTIONS.get(asked),
                        c,
                        questions);
            }
            if (unknown || skip) {
                c = c.with(asked, NOT_SPECIFIED);
            } else {
                Charter updated = apply(c, asked, userText);
                if (updated == null) {
                    return new AgentTurn("Sorry, I didn't catch that. " + QUESTIONS.get(asked), c, questions);
                }
                c = updated;
                questions.remove(openQuestion);
            }
            prefix = "Got it. ";
        }

        String next = nextField(c);
        if (next == null) {
            return new AgentTurn(
                    prefix + "That's everything I need. Review the charter on the right, then press "
                            + "\"Create project\", or tell me what to change.",
                    c,
                    questions);
        }
        String q = QUESTIONS.get(next);
        if (next.equals("name") && userText != null) q = "What should we call the project?";
        if (c.isReady() && next.equals("motivation")) {
            prefix += "The mandatory details are complete, so you can create the project now. ";
        }
        return new AgentTurn(prefix + q, c, questions);
    }

    @Override
    public String agentRef() {
        return "mock:scripted-v2";
    }

    /** Applies an answer; returns null when the answer can't be understood. */
    private static Charter apply(Charter c, String field, String answer) {
        return switch (field) {
            case "productType" -> c.with("productType", productTypeOf(answer)).with("productTypeDetails", answer);
            case "scale" -> {
                String scale = scaleOf(answer);
                yield scale == null ? null : c.with("scale", scale);
            }
            default -> c.with(field, answer);
        };
    }

    private static String nextField(Charter c) {
        for (String f : Charter.FIELDS) {
            if (c.get(f) == null) return f;
        }
        return null;
    }

    private static String lastUserMessage(List<ChatMessage> history) {
        if (history == null || history.isEmpty()) return null;
        ChatMessage last = history.get(history.size() - 1);
        return "user".equals(last.role())
                        && last.content() != null
                        && !last.content().isBlank()
                ? last.content().strip()
                : null;
    }

    private static boolean isUnknown(String s) {
        String t = s.toLowerCase(Locale.ROOT);
        return t.matches(".*\\b(don'?t know|not sure|no idea|unsure|tbd)\\b.*");
    }

    private static boolean isSkip(String s) {
        String t = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z ]", "").strip();
        return t.equals("skip") || t.equals("none") || t.equals("no") || t.equals("nothing") || t.equals("na");
    }

    static String productTypeOf(String answer) {
        String t = answer.toLowerCase(Locale.ROOT);
        if (t.matches(".*\\b(mobile|ios|android|iphone)\\b.*")) return "mobile_app";
        if (t.matches(".*\\b(web|website|site|portal|web app|webapp)\\b.*")) return "website";
        if (t.matches(".*\\b(api|backend|service|microservice)\\b.*")) return "api_backend";
        if (t.matches(".*\\b(desktop|windows|macos)\\b.*")) return "desktop_app";
        if (t.matches(".*\\b(internal|tool|admin)\\b.*")) return "internal_tool";
        if (t.matches(".*\\b(data|ai|ml|analytics|model)\\b.*")) return "data_ai";
        return "other";
    }

    static String scaleOf(String answer) {
        String t = answer.toLowerCase(Locale.ROOT);
        if (t.matches(".*\\b(public|external|customers?|everyone|consumers?|citizens?|patients?)\\b.*"))
            return "public";
        if (t.matches(".*\\b(company|organi[sz]ation|org|enterprise|whole|group[- ]wide)\\b.*")) return "organisation";
        if (t.matches(".*\\b(department|dept|division|business unit)\\b.*")) return "department";
        if (t.matches(".*\\b(team|squad)\\b.*")) return "team";
        if (t.matches(".*\\b(personal|me|myself|just me)\\b.*")) return "personal";
        return null;
    }
}
