package io.unitystack.platform.requirements.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenQuestionRepository extends JpaRepository<OpenQuestionEntity, UUID> {

    List<OpenQuestionEntity> findByProjectIdAndStatusOrderByCreatedAt(UUID projectId, String status);
}
