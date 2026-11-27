package io.unitystack.platform.requirements.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.unitystack.platform.core.common.ApiException;
import io.unitystack.platform.requirements.agents.MockInceptionAgent;
import io.unitystack.platform.requirements.api.UpdateProjectRequest;
import io.unitystack.platform.requirements.domain.Charter;
import io.unitystack.platform.requirements.persistence.AuditEventEntity;
import io.unitystack.platform.requirements.persistence.AuditEventRepository;
import io.unitystack.platform.requirements.persistence.CharterRevisionEntity;
import io.unitystack.platform.requirements.persistence.CharterRevisionRepository;
import io.unitystack.platform.requirements.persistence.InceptionConversationRepository;
import io.unitystack.platform.requirements.persistence.OpenQuestionRepository;
import io.unitystack.platform.requirements.persistence.ProjectEntity;
import io.unitystack.platform.requirements.persistence.ProjectRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/** FR-107 settings behaviour, with the repositories mocked. */
class ProjectServiceTest {
    private final ProjectRepository projects = mock(ProjectRepository.class);
    private final CharterRevisionRepository revisions = mock(CharterRevisionRepository.class);
    private final OpenQuestionRepository openQuestions = mock(OpenQuestionRepository.class);
    private final AuditEventRepository audit = mock(AuditEventRepository.class);
    private final ProjectService service = new ProjectService(projects, revisions, openQuestions,
            mock(InceptionConversationRepository.class), audit, new MockInceptionAgent(), "tester");

    private final UUID id = UUID.randomUUID();
    private final OffsetDateTime t0 = OffsetDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private final Charter charter = new Charter("GP Booking", "p", "u", "website", null, "b", "team", null, null, null);
    private ProjectEntity project;
    private CharterRevisionEntity rev1;

    @BeforeEach
    void setUp() {
        project = new ProjectEntity(id, "GB", "GP Booking", "ACTIVE", "website", "tester", t0);
        rev1 = new CharterRevisionEntity(id, 1, null, charter, "# md", "tester", "INCEPTION", "mock:scripted-v2", t0);
        when(projects.findById(id)).thenReturn(Optional.of(project));
        when(revisions.findFirstByProjectIdOrderByRevisionDesc(id)).thenReturn(Optional.of(rev1));
        when(openQuestions.findByProjectIdAndStatusOrderByCreatedAt(any(), any())).thenReturn(List.of());
        when(audit.findByProjectIdOrderByCreatedAtDesc(any(), any())).thenReturn(List.of());
    }

    @Test
    void renameInsertsANewRevisionLinkedToItsParent() {
        service.update(id, new UpdateProjectRequest("GP Bookings", null, null));

        ArgumentCaptor<CharterRevisionEntity> saved = ArgumentCaptor.forClass(CharterRevisionEntity.class);
        verify(revisions).save(saved.capture());
        CharterRevisionEntity rev2 = saved.getValue();
        assertThat(rev2.getId()).isNotEqualTo(rev1.getId());
        assertThat(rev2.getRevision()).isEqualTo(2);
        assertThat(rev2.getParentRevisionId()).isEqualTo(rev1.getId());
        assertThat(rev2.getContent().name()).isEqualTo("GP Bookings");
        assertThat(rev2.getSource()).isEqualTo("SETTINGS");
        assertThat(rev2.getAgentRef()).isNull();
        assertThat(rev1.getContent().name()).isEqualTo("GP Booking");
        assertThat(project.getName()).isEqualTo("GP Bookings");
        verify(audit).save(any(AuditEventEntity.class));
    }

    @Test
    void statusChangeAloneDoesNotCreateARevision() {
        service.update(id, new UpdateProjectRequest(null, null, "on_hold"));

        assertThat(project.getStatus()).isEqualTo("ON_HOLD");
        verify(revisions, never()).save(any());
        verify(audit).save(any(AuditEventEntity.class));
    }

    @Test
    void unchangedSettingsWriteNothing() {
        service.update(id, new UpdateProjectRequest("GP Booking", "gb", "ACTIVE"));

        verify(revisions, never()).save(any());
        verify(audit, never()).save(any());
        assertThat(project.getUpdatedAt()).isEqualTo(t0);
    }

    @Test
    void takenKeyIsRejected() {
        when(projects.existsByProjectKey("TAKEN")).thenReturn(true);
        assertThatThrownBy(() -> service.update(id, new UpdateProjectRequest(null, "TAKEN", null)))
                .isInstanceOf(ApiException.class)
                .hasMessage("Key TAKEN is already used");
    }
}
