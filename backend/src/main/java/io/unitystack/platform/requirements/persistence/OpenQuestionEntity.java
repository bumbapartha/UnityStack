package io.unitystack.platform.requirements.persistence;

import io.unitystack.platform.core.persistence.AssignedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

/** An unknown recorded during inception (FR-104). */
@Entity
@Table(name = "open_question")
public class OpenQuestionEntity extends AssignedIdEntity {
    public static final String OPEN = "OPEN";

    @Id
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected OpenQuestionEntity() {}

    public static OpenQuestionEntity open(UUID projectId, String question, OffsetDateTime at) {
        OpenQuestionEntity e = new OpenQuestionEntity();
        e.id = UUID.randomUUID();
        e.projectId = projectId;
        e.question = question;
        e.status = OPEN;
        e.createdAt = at;
        return e;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
