# Distribution and operations

[← Documentation index](../README.md)

Later (P2): installable distribution, platform operations and enterprise onboarding.

**Tools:** [Container registry](../tools-and-adapters.md#container-registry)

**Decisions:** [PD-5](../decisions/PD-5-open-source-defaults.md)

## Enterprise distribution and onboarding

- **Distribution:** versioned package; Terraform-provisioned AWS (EC2 + Docker Compose first, EKS later); S3 remote state with locking; IAM least privilege and GitHub OIDC; Secrets Manager or SSM; ECR; TLS; pinned versions in a release manifest; installer, upgrade, validate and uninstall scripts; full default-tool profile with health checks.
- **Platform lifecycle:** install, configure, upgrade, rollback/recovery and uninstall are separate from the lifecycle of managed software; a platform upgrade never triggers a product release. Irreversible migrations are never promised automatic rollback.
- **Adoption modes:** greenfield (deploy with default tools) or existing organisation (connect existing tools, ingest accessible history, keep teams' own processes).
- **Onboarding steps:** discover and configure providers and permissions; connect with least privilege, read-only first; start event capture before or with backfill; import history in resumable batches; normalise records while keeping source IDs and provenance; build verified links, then optionally propose inferred ones; report completeness and gaps; enable steady-state sync and health alerts.
- **Tenancy:** one independent installation per organisation; multi-tenant SaaS only if later required.

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-001 | P2 | Deliver a versioned UnityStack distribution with infrastructure modules, deployment definitions, configuration templates and installer scripts. *(Was P0.)* |
| FR-002 | P2 | Accept organisation-specific cloud region, domain, GitHub organisation, AI provider and deployment profile settings. *(Was P0.)* |
| FR-003 | P1 | Use Terraform to provision required AWS resources with repeatable plan and apply workflows. *(Was P0; needed as soon as the thin loop works, [AD-2](../decisions/AD-2-docker-then-aws.md).)* |
| FR-004 | P2 | A full installation profile deploys UnityStack and the default tool for every selected capability ([Tools and adapters](../tools-and-adapters.md)), with their dependencies. *(Was P0.)* |
| FR-035 | P2 | Deploy and expose the default tools chosen at install time. *(Was P0.)* |
| FR-006 | P2 | Make installation idempotent where practical; rerunning must not destroy persistent data. *(Was P0.)* |
| FR-007 | P2 | Pin application and infrastructure versions in a release manifest. *(Was P0.)* |
| FR-008 | P1 | Provide backup and restore of UnityStack data, health checks and documented upgrade steps. *(Was P0.)* |
| FR-009 | P2 | Support optional core and custom installation profiles. |
| FR-010 | P2 | Provide scheduled drift checks, update recommendations and operational dashboards. |
| FR-048 | P2 | Separate platform installation/upgrade/recovery from the lifecycle of customer software, with separate versions, roles and audit records. *(Was P0.)* |
| FR-054 | P2 | Record installation phase outcomes, readiness failures, backup verification and repeat-deployment results in an inspectable report. *(Was P0.)* |
| FR-057 | P2 | Provide onboarding templates and validation for domain, region, repositories, identity, AI settings and tool endpoints. *(Was P1.)* |
| FR-059 | P2 | Provide cost and resource reporting and sizing profiles based on measured usage. *(Was P1.)* |
