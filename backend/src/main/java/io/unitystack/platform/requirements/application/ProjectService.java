package io.unitystack.platform.requirements.application;

import io.unitystack.platform.core.common.ApiException;
import io.unitystack.platform.requirements.agents.InceptionAgent;
import io.unitystack.platform.requirements.api.ActivityEntry;
import io.unitystack.platform.requirements.api.CreateProjectRequest;
import io.unitystack.platform.requirements.api.ProjectDetail;
import io.unitystack.platform.requirements.api.ProjectSummary;
import io.unitystack.platform.requirements.api.UpdateProjectRequest;
import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.domain.CharterMarkdown;
import io.unitystack.platform.requirements.domain.ProjectKeys;
import io.unitystack.platform.requirements.persistence.AuditEventEntity;
import io.unitystack.platform.requirements.persistence.AuditEventRepository;
import io.unitystack.platform.requirements.persistence.CharterRevisionEntity;
import io.unitystack.platform.requirements.persistence.CharterRevisionRepository;
import io.unitystack.platform.requirements.persistence.InceptionConversationEntity;
import io.unitystack.platform.requirements.persistence.InceptionConversationRepository;
import io.unitystack.platform.requirements.persistence.OpenQuestionEntity;
import io.unitystack.platform.requirements.persistence.OpenQuestionRepository;
import io.unitystack.platform.requirements.persistence.ProjectEntity;
import io.unitystack.platform.requirements.persistence.ProjectRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    public static final Set<String> STATUSES = Set.of("ACTIVE", "ON_HOLD", "ARCHIVED");
    static final int ACTIVITY_LIMIT = 50;

    private final ProjectRepository projects;
    private final CharterRevisionRepository revisions;
    private final OpenQuestionRepository openQuestions;
    private final InceptionConversationRepository conversations;
    private final AuditEventRepository audit;
    private final InceptionAgent agent;
    private final String localUser;

    public ProjectService(
            ProjectRepository projects,
            CharterRevisionRepository revisions,
            OpenQuestionRepository openQuestions,
            InceptionConversationRepository conversations,
            AuditEventRepository audit,
            InceptionAgent agent,
            @Value("${unitystack.local-user}") String localUser) {
        this.projects = projects;
        this.revisions = revisions;
        this.openQuestions = openQuestions;
        this.conversations = conversations;
        this.audit = audit;
        this.agent = agent;
        this.localUser = localUser;
    }

    /** FR-105/FR-106: create only from a confirmed charter with all mandatory items. */
    @Transactional
    public ProjectDetail create(CreateProjectRequest req) {
        if (req.charter() == null) throw new ApiException(HttpStatus.BAD_REQUEST, "Charter is required");
        Charter charter = req.charter().normalised();
        if (!charter.isReady()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Charter is missing mandatory items: " + String.join(", ", charter.missingMandatory()));
        }
        List<String> questions = req.openQuestions() == null
                ? List.of()
                : req.openQuestions().stream()
                        .map(q -> q == null ? "" : q.strip())
                        .filter(q -> !q.isEmpty())
                        .distinct()
                        .limit(20)
                        .toList();

        OffsetDateTime now = now();
        UUID id = UUID.randomUUID();
        String key = ProjectKeys.generate(charter.name(), projects::existsByProjectKey);
        projects.save(new ProjectEntity(id, key, charter.name(), "ACTIVE", charter.productType(), localUser, now));
        revisions.save(new CharterRevisionEntity(
                id,
                1,
                null,
                charter,
                CharterMarkdown.render(charter, key, questions),
                localUser,
                "INCEPTION",
                agent.agentRef(),
                now));
        questions.forEach(q -> openQuestions.save(OpenQuestionEntity.open(id, q, now)));
        conversations.save(new InceptionConversationEntity(
                id, req.transcript() == null ? List.of() : req.transcript(), agent.agentRef(), now));
        audit.save(new AuditEventEntity(
                id,
                localUser,
                "PROJECT_CREATED",
                Map.of("key", key, "name", charter.name(), "agentRef", agent.agentRef()),
                now));
        return get(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectSummary> list() {
        return projects.findAllByOrderByCreatedAtDesc().stream()
                .map(ProjectService::summary)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetail get(UUID id) {
        ProjectEntity p =
                projects.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
        CharterRevisionEntity rev = revisions
                .findFirstByProjectIdOrderByRevisionDesc(id)
                .orElseThrow(() -> new IllegalStateException("Project without charter: " + id));
        List<ActivityEntry> activity = audit.findByProjectIdOrderByCreatedAtDesc(id, Limit.of(ACTIVITY_LIMIT)).stream()
                .map(a -> new ActivityEntry(a.getActor(), a.getAction(), a.getDetails(), a.getCreatedAt()))
                .toList();
        return new ProjectDetail(
                summary(p), rev.getRevision(), rev.getContent(), rev.getMarkdown(), openQuestions(id), activity);
    }

    /** FR-107: simple settings panel. A rename also produces a new charter revision. */
    @Transactional
    public ProjectDetail update(UUID id, UpdateProjectRequest req) {
        ProjectEntity p =
                projects.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
        String name = req.name() == null ? p.getName() : req.name().strip();
        String key = req.key() == null ? p.getProjectKey() : req.key().strip().toUpperCase();
        String status =
                req.status() == null ? p.getStatus() : req.status().strip().toUpperCase();

        if (name.isEmpty() || name.length() > 200)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Name must be 1-200 characters");
        if (!ProjectKeys.VALID.matcher(key).matches()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST, "Key must be 2-10 characters: a letter followed by letters or digits");
        }
        if (!key.equals(p.getProjectKey()) && projects.existsByProjectKey(key)) {
            throw new ApiException(HttpStatus.CONFLICT, "Key " + key + " is already used");
        }
        if (!STATUSES.contains(status))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Status must be one of " + STATUSES);

        Map<String, Object> changes = new LinkedHashMap<>();
        if (!name.equals(p.getName())) changes.put("name", Map.of("from", p.getName(), "to", name));
        if (!key.equals(p.getProjectKey())) changes.put("key", Map.of("from", p.getProjectKey(), "to", key));
        if (!status.equals(p.getStatus())) changes.put("status", Map.of("from", p.getStatus(), "to", status));
        if (changes.isEmpty()) return get(id);

        OffsetDateTime now = now();
        p.changeSettings(key, name, status, now);
        if (changes.containsKey("name") || changes.containsKey("key")) {
            // Revisions are new rows that point to their parent; the latest one is never changed (TD-12).
            CharterRevisionEntity latest =
                    revisions.findFirstByProjectIdOrderByRevisionDesc(id).orElseThrow();
            Charter charter = latest.getContent().withName(name);
            revisions.save(latest.next(
                    charter,
                    CharterMarkdown.render(charter, key, openQuestions(id)),
                    localUser,
                    "SETTINGS",
                    null,
                    now));
        }
        audit.save(new AuditEventEntity(id, localUser, "PROJECT_SETTINGS_CHANGED", changes, now));
        return get(id);
    }

    private List<String> openQuestions(UUID projectId) {
        return openQuestions.findByProjectIdAndStatusOrderByCreatedAt(projectId, OpenQuestionEntity.OPEN).stream()
                .map(OpenQuestionEntity::getQuestion)
                .toList();
    }

    private static ProjectSummary summary(ProjectEntity p) {
        return new ProjectSummary(
                p.getId(),
                p.getProjectKey(),
                p.getName(),
                p.getStatus(),
                p.getProductType(),
                p.getCreatedBy(),
                p.getCreatedAt());
    }

    private static OffsetDateTime now() {
        return OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
    }
}
