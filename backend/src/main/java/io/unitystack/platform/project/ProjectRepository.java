package io.unitystack.platform.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.unitystack.platform.inception.Charter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository {
    private final JdbcClient jdbc;
    private final ObjectMapper json;

    public ProjectRepository(JdbcClient jdbc, ObjectMapper json) {
        this.jdbc = jdbc;
        this.json = json;
    }

    public record ProjectRow(UUID id, String key, String name, String status, String productType,
                             String createdBy, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    }

    public record RevisionRow(UUID id, int revision, Charter charter, String markdown, String actor,
                              String source, String agentRef, OffsetDateTime createdAt) {
    }

    public record AuditRow(String actor, String action, Map<String, Object> details, OffsetDateTime createdAt) {
    }

    public boolean keyExists(String key) {
        return jdbc.sql("select count(*) from project where project_key = :k").param("k", key)
                .query(Integer.class).single() > 0;
    }

    public void insertProject(ProjectRow p) {
        jdbc.sql("""
                insert into project (id, project_key, name, status, product_type, created_by, created_at, updated_at)
                values (:id, :key, :name, :status, :type, :by, :created, :updated)""")
                .param("id", p.id()).param("key", p.key()).param("name", p.name()).param("status", p.status())
                .param("type", p.productType()).param("by", p.createdBy())
                .param("created", p.createdAt()).param("updated", p.updatedAt())
                .update();
    }

    public void updateProject(UUID id, String key, String name, String status, OffsetDateTime updatedAt) {
        jdbc.sql("update project set project_key = :key, name = :name, status = :status, updated_at = :u where id = :id")
                .param("key", key).param("name", name).param("status", status).param("u", updatedAt).param("id", id)
                .update();
    }

    public void insertRevision(UUID projectId, UUID id, int revision, UUID parentId, Charter charter, String markdown,
                               String actor, String source, String agentRef, OffsetDateTime at) {
        jdbc.sql("""
                insert into charter_revision (id, project_id, revision, parent_revision_id, content, markdown, actor, source, agent_ref, created_at)
                values (:id, :pid, :rev, :parent, cast(:content as jsonb), :md, :actor, :source, :agent, :at)""")
                .param("id", id).param("pid", projectId).param("rev", revision).param("parent", parentId)
                .param("content", toJson(charter)).param("md", markdown).param("actor", actor)
                .param("source", source).param("agent", agentRef).param("at", at)
                .update();
    }

    public void insertOpenQuestion(UUID projectId, String question, OffsetDateTime at) {
        jdbc.sql("insert into open_question (id, project_id, question, status, created_at) values (:id, :pid, :q, 'OPEN', :at)")
                .param("id", UUID.randomUUID()).param("pid", projectId).param("q", question).param("at", at)
                .update();
    }

    public void insertConversation(UUID projectId, Object transcript, String agentRef, OffsetDateTime at) {
        jdbc.sql("insert into inception_conversation (project_id, transcript, agent_ref, created_at) values (:pid, cast(:t as jsonb), :agent, :at)")
                .param("pid", projectId).param("t", toJson(transcript)).param("agent", agentRef).param("at", at)
                .update();
    }

    public void insertAudit(UUID projectId, String actor, String action, Map<String, Object> details, OffsetDateTime at) {
        jdbc.sql("insert into audit_event (id, project_id, actor, action, details, created_at) values (:id, :pid, :actor, :action, cast(:d as jsonb), :at)")
                .param("id", UUID.randomUUID()).param("pid", projectId).param("actor", actor).param("action", action)
                .param("d", toJson(details)).param("at", at)
                .update();
    }

    public List<ProjectRow> findAll() {
        return jdbc.sql("select * from project order by created_at desc").query(this::project).list();
    }

    public Optional<ProjectRow> find(UUID id) {
        return jdbc.sql("select * from project where id = :id").param("id", id).query(this::project).optional();
    }

    public Optional<RevisionRow> latestRevision(UUID projectId) {
        return jdbc.sql("select * from charter_revision where project_id = :pid order by revision desc limit 1")
                .param("pid", projectId)
                .query((rs, n) -> new RevisionRow(
                        rs.getObject("id", UUID.class), rs.getInt("revision"),
                        fromJson(rs.getString("content"), Charter.class), rs.getString("markdown"),
                        rs.getString("actor"), rs.getString("source"), rs.getString("agent_ref"),
                        rs.getObject("created_at", OffsetDateTime.class)))
                .optional();
    }

    public List<String> openQuestions(UUID projectId) {
        return jdbc.sql("select question from open_question where project_id = :pid and status = 'OPEN' order by created_at")
                .param("pid", projectId).query(String.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<AuditRow> auditTrail(UUID projectId, int limit) {
        return jdbc.sql("select * from audit_event where project_id = :pid order by created_at desc limit :lim")
                .param("pid", projectId).param("lim", limit)
                .query((rs, n) -> new AuditRow(rs.getString("actor"), rs.getString("action"),
                        fromJson(rs.getString("details"), Map.class), rs.getObject("created_at", OffsetDateTime.class)))
                .list();
    }

    private ProjectRow project(java.sql.ResultSet rs, int n) throws java.sql.SQLException {
        return new ProjectRow(rs.getObject("id", UUID.class), rs.getString("project_key"), rs.getString("name"),
                rs.getString("status"), rs.getString("product_type"), rs.getString("created_by"),
                rs.getObject("created_at", OffsetDateTime.class), rs.getObject("updated_at", OffsetDateTime.class));
    }

    private String toJson(Object o) {
        try {
            return json.writeValueAsString(o);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> T fromJson(String s, Class<T> type) {
        try {
            return json.readValue(s, type);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
