package io.unitystack.platform.requirements.persistence;

import io.unitystack.platform.core.persistence.AssignedIdEntity;
import io.unitystack.platform.requirements.domain.Charter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * One charter revision. Immutable: a change inserts a new row that points to its parent (TD-12). The charter
 * itself is stored as JSONB so new charter fields need no migration.
 */
@Entity
@Immutable
@Table(name = "charter_revision")
public class CharterRevisionEntity extends AssignedIdEntity {

    @Id
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(nullable = false)
    private int revision;

    @Column(name = "parent_revision_id")
    private UUID parentRevisionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private Charter content;

    @Column(nullable = false, columnDefinition = "text")
    private String markdown;

    @Column(nullable = false, length = 100)
    private String actor;

    /** INCEPTION | SETTINGS | AI_CHAT */
    @Column(nullable = false, length = 30)
    private String source;

    @Column(name = "agent_ref", length = 200)
    private String agentRef;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected CharterRevisionEntity() {
    }

    public CharterRevisionEntity(UUID projectId, int revision, UUID parentRevisionId, Charter content, String markdown,
                                 String actor, String source, String agentRef, OffsetDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.projectId = projectId;
        this.revision = revision;
        this.parentRevisionId = parentRevisionId;
        this.content = content;
        this.markdown = markdown;
        this.actor = actor;
        this.source = source;
        this.agentRef = agentRef;
        this.createdAt = createdAt;
    }

    /** The next revision, linked to this one as its parent. */
    public CharterRevisionEntity next(Charter content, String markdown, String actor, String source, String agentRef,
                                      OffsetDateTime at) {
        return new CharterRevisionEntity(projectId, revision + 1, id, content, markdown, actor, source, agentRef, at);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public int getRevision() {
        return revision;
    }

    public UUID getParentRevisionId() {
        return parentRevisionId;
    }

    public Charter getContent() {
        return content;
    }

    public String getMarkdown() {
        return markdown;
    }

    public String getActor() {
        return actor;
    }

    public String getSource() {
        return source;
    }

    public String getAgentRef() {
        return agentRef;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
