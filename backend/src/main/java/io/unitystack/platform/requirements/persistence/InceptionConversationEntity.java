package io.unitystack.platform.requirements.persistence;

import io.unitystack.platform.core.persistence.AssignedIdEntity;
import io.unitystack.platform.requirements.domain.ChatMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** The confirmed inception transcript, kept for audit (FR-105). One per project. */
@Entity
@Immutable
@Table(name = "inception_conversation")
public class InceptionConversationEntity extends AssignedIdEntity {

    @Id
    @Column(name = "project_id")
    private UUID projectId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<ChatMessage> transcript;

    @Column(name = "agent_ref", length = 200)
    private String agentRef;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected InceptionConversationEntity() {}

    public InceptionConversationEntity(
            UUID projectId, List<ChatMessage> transcript, String agentRef, OffsetDateTime createdAt) {
        this.projectId = projectId;
        this.transcript = transcript;
        this.agentRef = agentRef;
        this.createdAt = createdAt;
    }

    @Override
    public UUID getId() {
        return projectId;
    }

    public List<ChatMessage> getTranscript() {
        return transcript;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
