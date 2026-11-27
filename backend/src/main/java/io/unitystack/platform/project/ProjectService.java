package io.unitystack.platform.project;

import io.unitystack.platform.ai.InceptionAgent;
import io.unitystack.platform.common.ApiException;
import io.unitystack.platform.inception.Charter;
import io.unitystack.platform.inception.CharterMarkdown;
import io.unitystack.platform.inception.ChatMessage;
import io.unitystack.platform.project.ProjectRepository.AuditRow;
import io.unitystack.platform.project.ProjectRepository.ProjectRow;
import io.unitystack.platform.project.ProjectRepository.RevisionRow;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    public static final Set<String> STATUSES = Set.of("ACTIVE", "ON_HOLD", "ARCHIVED");

    private final ProjectRepository repo;
    private final InceptionAgent agent;
    private final String localUser;

    public ProjectService(ProjectRepository repo, InceptionAgent agent, @Value("${unitystack.local-user}") String localUser) {
        this.repo = repo;
        this.agent = agent;
        this.localUser = localUser;
    }

    public record CreateRequest(Charter charter, List<String> openQuestions, List<ChatMessage> transcript) {
    }

    public record UpdateRequest(String name, String key, String status) {
    }

    public record ProjectSummary(UUID id, String key, String name, String status, String productType,
                                 String createdBy, OffsetDateTime createdAt) {
    }

    public record ProjectDetail(ProjectSummary project, int charterRevision, Charter charter, String charterMarkdown,
                                List<String> openQuestions, List<AuditRow> activity) {
    }

    /** FR-105/FR-106: create only from a confirmed charter with all mandatory items. */
    @Transactional
    public ProjectDetail create(CreateRequest req) {
        if (req.charter() == null) throw new ApiException(HttpStatus.BAD_REQUEST, "Charter is required");
        Charter charter = req.charter().normalised();
        if (!charter.isReady()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Charter is missing mandatory items: " + String.join(", ", charter.missingMandatory()));
        }
        List<String> questions = req.openQuestions() == null ? List.of()
                : req.openQuestions().stream().map(q -> q == null ? "" : q.strip()).filter(q -> !q.isEmpty()).distinct().limit(20).toList();

        OffsetDateTime now = now();
        UUID id = UUID.randomUUID();
        String key = ProjectKeys.generate(charter.name(), repo::keyExists);
        repo.insertProject(new ProjectRow(id, key, charter.name(), "ACTIVE", charter.productType(), localUser, now, now));
        repo.insertRevision(id, UUID.randomUUID(), 1, null, charter, CharterMarkdown.render(charter, key, questions),
                localUser, "INCEPTION", agent.agentRef(), now);
        questions.forEach(q -> repo.insertOpenQuestion(id, q, now));
        repo.insertConversation(id, req.transcript() == null ? List.of() : req.transcript(), agent.agentRef(), now);
        repo.insertAudit(id, localUser, "PROJECT_CREATED", Map.of("key", key, "name", charter.name(), "agentRef", agent.agentRef()), now);
        return get(id);
    }

    public List<ProjectSummary> list() {
        return repo.findAll().stream().map(ProjectService::summary).toList();
    }

    public ProjectDetail get(UUID id) {
        ProjectRow p = repo.find(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
        RevisionRow rev = repo.latestRevision(id).orElseThrow(() -> new IllegalStateException("Project without charter: " + id));
        return new ProjectDetail(summary(p), rev.revision(), rev.charter(), rev.markdown(), repo.openQuestions(id), repo.auditTrail(id, 50));
    }

    /** FR-107: simple settings panel. A rename also produces a new charter revision. */
    @Transactional
    public ProjectDetail update(UUID id, UpdateRequest req) {
        ProjectRow p = repo.find(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Project not found"));
        String name = req.name() == null ? p.name() : req.name().strip();
        String key = req.key() == null ? p.key() : req.key().strip().toUpperCase();
        String status = req.status() == null ? p.status() : req.status().strip().toUpperCase();

        if (name.isEmpty() || name.length() > 200) throw new ApiException(HttpStatus.BAD_REQUEST, "Name must be 1-200 characters");
        if (!ProjectKeys.VALID.matcher(key).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Key must be 2-10 characters: a letter followed by letters or digits");
        }
        if (!key.equals(p.key()) && repo.keyExists(key)) throw new ApiException(HttpStatus.CONFLICT, "Key " + key + " is already used");
        if (!STATUSES.contains(status)) throw new ApiException(HttpStatus.BAD_REQUEST, "Status must be one of " + STATUSES);

        Map<String, Object> changes = new LinkedHashMap<>();
        if (!name.equals(p.name())) changes.put("name", Map.of("from", p.name(), "to", name));
        if (!key.equals(p.key())) changes.put("key", Map.of("from", p.key(), "to", key));
        if (!status.equals(p.status())) changes.put("status", Map.of("from", p.status(), "to", status));
        if (changes.isEmpty()) return get(id);

        OffsetDateTime now = now();
        repo.updateProject(id, key, name, status, now);
        if (changes.containsKey("name") || changes.containsKey("key")) {
            RevisionRow latest = repo.latestRevision(id).orElseThrow();
            Charter charter = latest.charter().withName(name);
            repo.insertRevision(id, UUID.randomUUID(), latest.revision() + 1, latest.id(), charter,
                    CharterMarkdown.render(charter, key, repo.openQuestions(id)), localUser, "SETTINGS", null, now);
        }
        repo.insertAudit(id, localUser, "PROJECT_SETTINGS_CHANGED", changes, now);
        return get(id);
    }

    private static ProjectSummary summary(ProjectRow p) {
        return new ProjectSummary(p.id(), p.key(), p.name(), p.status(), p.productType(), p.createdBy(), p.createdAt());
    }

    private static OffsetDateTime now() {
        return OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
    }
}
