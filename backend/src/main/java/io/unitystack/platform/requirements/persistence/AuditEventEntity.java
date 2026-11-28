package io.unitystack.platform.requirements.persistence;

import io.unitystack.platform.core.persistence.AssignedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** An append-only audit record for a project. */
@Entity
@Immutable
@Table(name = "audit_event")
public class AuditEventEntity extends AssignedIdEntity {

    @Id
    private UUID id;

    @Column(name = "project_id")
    private UUID projectId;

    @Column(nullable = false, length = 100)
    private String actor;

    @Column(nullable = false, length = 50)
    private String action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private Map<String, Object> details;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected AuditEventEntity() {
    }

    public AuditEventEntity(UUID projectId, String actor, String action, Map<String, Object> details, OffsetDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.projectId = projectId;
        this.actor = actor;
        this.action = action;
        this.details = details;
        this.createdAt = createdAt;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getActor() {
        return actor;
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
