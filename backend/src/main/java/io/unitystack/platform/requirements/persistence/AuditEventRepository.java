package io.unitystack.platform.requirements.persistence;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventRepository extends JpaRepository<AuditEventEntity, UUID> {

    /**
     * Newest first. A projection, so the values are always read back from the database (including JSONB
     * key order) even straight after an insert in the same transaction.
     */
    List<AuditEventView> findByProjectIdOrderByCreatedAtDesc(UUID projectId, Limit limit);

    interface AuditEventView {
        String getActor();

        String getAction();

        Map<String, Object> getDetails();

        OffsetDateTime getCreatedAt();
    }
}
