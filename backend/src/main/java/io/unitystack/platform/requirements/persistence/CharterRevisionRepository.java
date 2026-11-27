package io.unitystack.platform.requirements.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharterRevisionRepository extends JpaRepository<CharterRevisionEntity, UUID> {

    Optional<CharterRevisionEntity> findFirstByProjectIdOrderByRevisionDesc(UUID projectId);
}
