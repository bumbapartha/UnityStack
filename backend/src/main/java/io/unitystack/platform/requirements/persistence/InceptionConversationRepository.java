package io.unitystack.platform.requirements.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InceptionConversationRepository extends JpaRepository<InceptionConversationEntity, UUID> {}
