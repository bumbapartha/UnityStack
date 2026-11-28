package io.unitystack.platform.requirements.persistence;

import io.unitystack.platform.core.persistence.AssignedIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

/** A project created through inception (FR-105). Internal to the requirements module. */
@Entity
@Table(name = "project")
public class ProjectEntity extends AssignedIdEntity {

    @Id
    private UUID id;

    @Column(name = "project_key", nullable = false, unique = true, length = 10)
    private String projectKey;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "product_type", nullable = false, length = 30)
    private String productType;

    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected ProjectEntity() {}

    public ProjectEntity(
            UUID id,
            String projectKey,
            String name,
            String status,
            String productType,
            String createdBy,
            OffsetDateTime createdAt) {
        this.id = id;
        this.projectKey = projectKey;
        this.name = name;
        this.status = status;
        this.productType = productType;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    /** FR-107: settings change. */
    public void changeSettings(String projectKey, String name, String status, OffsetDateTime at) {
        this.projectKey = projectKey;
        this.name = name;
        this.status = status;
        this.updatedAt = at;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getProductType() {
        return productType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
