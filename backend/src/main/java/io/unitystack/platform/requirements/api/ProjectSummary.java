package io.unitystack.platform.requirements.api;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProjectSummary(UUID id, String key, String name, String status, String productType,
                             String createdBy, OffsetDateTime createdAt) {
}
