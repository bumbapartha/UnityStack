package io.unitystack.platform.requirements.api;

import io.unitystack.platform.requirements.domain.Charter;
import java.util.List;

public record ProjectDetail(
        ProjectSummary project,
        int charterRevision,
        Charter charter,
        String charterMarkdown,
        List<String> openQuestions,
        List<ActivityEntry> activity) {}
