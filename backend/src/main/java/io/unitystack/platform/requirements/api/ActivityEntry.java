package io.unitystack.platform.requirements.api;

import java.time.OffsetDateTime;
import java.util.Map;

/** One audit event shown in a project's activity list. */
public record ActivityEntry(String actor, String action, Map<String, Object> details, OffsetDateTime createdAt) {}
