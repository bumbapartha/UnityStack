package io.unitystack.platform.requirements.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    boolean existsByProjectKey(String projectKey);

    List<ProjectEntity> findAllByOrderByCreatedAtDesc();
}
