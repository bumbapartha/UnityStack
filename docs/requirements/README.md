# Requirements

[← Documentation index](../README.md)

Requirements are grouped by SDLC phase, in the order work flows through the loop, followed by areas that cut across every phase.

## SDLC phases

| #   | SDLC phase                | File                                                               | Requirements |
| --- | ------------------------- | ------------------------------------------------------------------ | ------------ |
| 1   | Requirements              | [01-requirements.md](01-requirements.md)                           | 21           |
| 2   | Architecture and design   | [02-architecture-and-design.md](02-architecture-and-design.md)     | 5            |
| 3   | Development               | [03-development.md](03-development.md)                             | 12           |
| 4   | Testing                   | [04-testing.md](04-testing.md)                                     | 2            |
| 5   | Deployment                | [05-deployment.md](05-deployment.md)                               | 10           |
| 6   | Monitoring and operations | [06-monitoring-and-operations.md](06-monitoring-and-operations.md) | 7            |

## Cross-cutting areas

| Area                         | File                                                                     | Requirements |
| ---------------------------- | ------------------------------------------------------------------------ | ------------ |
| Security                     | [07-security.md](07-security.md)                                         | 5            |
| Communication                | [08-communication.md](08-communication.md)                               | 2            |
| Traceability and integration | [09-traceability-and-integration.md](09-traceability-and-integration.md) | 31           |
| Core platform                | [10-core-platform.md](10-core-platform.md)                               | 30           |
| Distribution and onboarding  | [11-distribution-and-onboarding.md](11-distribution-and-onboarding.md)   | 14           |
| Non-functional requirements  | [12-non-functional.md](12-non-functional.md)                             | 18           |

## How to read the requirements

- **Requirement IDs are stable.** Every [FR-001](11-distribution-and-onboarding.md) to [FR-108](01-requirements.md) ID from earlier versions is kept, so existing references stay valid. New requirements start at [FR-109](10-core-platform.md). IDs are grouped by SDLC phase, one file per phase, rather than by the version that introduced them.
- **Priorities have been reset** to match the product decisions in [Decisions](../decisions.md) (see [Scope and roadmap](../roadmap.md) for what P0, P1 and P2 now mean).
- **"GitHub" means the connected Git provider.** GitHub is the first Git provider adapter ([AD-4](../decisions.md#ad-4)). Where this document says GitHub, PR or GitHub App, read the equivalent in the connected provider (for example a GitLab merge request) unless it names a GitHub-only feature.
- **Detailed design** (C4 diagrams, API schemas, Terraform modules, threat models) belongs in separate HLD and ADR documents, linked from Git.
- [Project Inception](01-requirements.md#project-inception-built) is the only area currently specified at the slice level. Other areas will gain slice-level detail as they are built.
- Priorities marked _(Was …)_ show the earlier priority where it changed.

### Priorities

Priorities in this document mean:

| Priority       | Meaning                                                                                                                    |
| -------------- | -------------------------------------------------------------------------------------------------------------------------- |
| **P0 — MVP**   | Needed for the thin closed loop, used by the sole user ([PD-1](../decisions.md#pd-1), [PD-3](../decisions.md#pd-3)).       |
| **P1 — Next**  | Deepens the loop while dogfooding and prepares for a small group of demo users.                                            |
| **P2 — Later** | Enterprise distribution and adoption: installer, full default-tool bundle, BYOT selection, SSO, HA, large-scale ingestion. |

## ID index

| ID                                           | Priority | Phase or area                |
| -------------------------------------------- | -------- | ---------------------------- |
| [FR-001](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-002](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-003](11-distribution-and-onboarding.md)  | P1       | Distribution and onboarding  |
| [FR-004](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-005](10-core-platform.md)                | P0       | Core platform                |
| [FR-006](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-007](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-008](11-distribution-and-onboarding.md)  | P1       | Distribution and onboarding  |
| [FR-009](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-010](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-011](01-requirements.md)                 | P0       | Requirements                 |
| [FR-012](01-requirements.md)                 | P0       | Requirements                 |
| [FR-013](01-requirements.md)                 | P0       | Requirements                 |
| [FR-014](01-requirements.md)                 | P0       | Requirements                 |
| [FR-015](01-requirements.md)                 | P1       | Requirements                 |
| [FR-016](01-requirements.md)                 | P2       | Requirements                 |
| [FR-017](03-development.md)                  | P2       | Development                  |
| [FR-018](03-development.md)                  | P0       | Development                  |
| [FR-019](03-development.md)                  | P0       | Development                  |
| [FR-020](10-core-platform.md)                | P0       | Core platform                |
| [FR-021](03-development.md)                  | P0       | Development                  |
| [FR-022](03-development.md)                  | P0       | Development                  |
| [FR-023](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-024](03-development.md)                  | P1       | Development                  |
| [FR-025](03-development.md)                  | P2       | Development                  |
| [FR-026](10-core-platform.md)                | P0       | Core platform                |
| [FR-027](10-core-platform.md)                | P0       | Core platform                |
| [FR-028](10-core-platform.md)                | P0       | Core platform                |
| [FR-029](10-core-platform.md)                | P0       | Core platform                |
| [FR-030](10-core-platform.md)                | P0       | Core platform                |
| [FR-031](10-core-platform.md)                | P0       | Core platform                |
| [FR-032](10-core-platform.md)                | P1       | Core platform                |
| [FR-033](10-core-platform.md)                | P1       | Core platform                |
| [FR-034](10-core-platform.md)                | P1       | Core platform                |
| [FR-035](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-036](01-requirements.md)                 | P0       | Requirements                 |
| [FR-037](02-architecture-and-design.md)      | P0       | Architecture and design      |
| [FR-038](02-architecture-and-design.md)      | P0       | Architecture and design      |
| [FR-039](02-architecture-and-design.md)      | P0       | Architecture and design      |
| [FR-040](02-architecture-and-design.md)      | P2       | Architecture and design      |
| [FR-041](04-testing.md)                      | P1       | Testing                      |
| [FR-042](10-core-platform.md)                | P0       | Core platform                |
| [FR-043](10-core-platform.md)                | P2       | Core platform                |
| [FR-044](05-deployment.md)                   | P0       | Deployment                   |
| [FR-045](05-deployment.md)                   | P0       | Deployment                   |
| [FR-046](06-monitoring-and-operations.md)    | P0       | Monitoring and operations    |
| [FR-047](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-048](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-049](07-security.md)                     | P0       | Security                     |
| [FR-050](10-core-platform.md)                | P0       | Core platform                |
| [FR-051](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-052](07-security.md)                     | P0       | Security                     |
| [FR-053](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-054](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-055](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-056](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-057](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-058](03-development.md)                  | P2       | Development                  |
| [FR-059](11-distribution-and-onboarding.md)  | P2       | Distribution and onboarding  |
| [FR-060](10-core-platform.md)                | P2       | Core platform                |
| [FR-061](01-requirements.md)                 | P0       | Requirements                 |
| [FR-062](01-requirements.md)                 | P0       | Requirements                 |
| [FR-063](01-requirements.md)                 | P0       | Requirements                 |
| [FR-064](10-core-platform.md)                | P0       | Core platform                |
| [FR-065](10-core-platform.md)                | P0       | Core platform                |
| [FR-066](03-development.md)                  | P1       | Development                  |
| [FR-067](03-development.md)                  | P1       | Development                  |
| [FR-068](01-requirements.md)                 | P0       | Requirements                 |
| [FR-069](01-requirements.md)                 | P0       | Requirements                 |
| [FR-070](01-requirements.md)                 | P0       | Requirements                 |
| [FR-071](10-core-platform.md)                | P0       | Core platform                |
| [FR-072](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-073](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-074](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-075](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-076](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-077](10-core-platform.md)                | P2       | Core platform                |
| [FR-078](10-core-platform.md)                | P2       | Core platform                |
| [FR-079](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-080](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-081](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-082](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-083](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-084](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-085](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-086](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-087](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-088](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-089](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-090](09-traceability-and-integration.md) | P2       | Traceability and integration |
| [FR-091](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-092](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-093](10-core-platform.md)                | P0       | Core platform                |
| [FR-094](10-core-platform.md)                | P0       | Core platform                |
| [FR-095](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-096](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-097](05-deployment.md)                   | P0       | Deployment                   |
| [FR-098](05-deployment.md)                   | P0       | Deployment                   |
| [FR-099](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-100](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-101](09-traceability-and-integration.md) | P1       | Traceability and integration |
| [FR-102](01-requirements.md)                 | P0       | Requirements                 |
| [FR-103](01-requirements.md)                 | P0       | Requirements                 |
| [FR-104](01-requirements.md)                 | P0       | Requirements                 |
| [FR-105](01-requirements.md)                 | P0       | Requirements                 |
| [FR-106](01-requirements.md)                 | P0       | Requirements                 |
| [FR-107](01-requirements.md)                 | P0       | Requirements                 |
| [FR-108](01-requirements.md)                 | P1       | Requirements                 |
| [FR-109](10-core-platform.md)                | P0       | Core platform                |
| [FR-110](10-core-platform.md)                | P0       | Core platform                |
| [FR-111](10-core-platform.md)                | P0       | Core platform                |
| [FR-112](10-core-platform.md)                | P1       | Core platform                |
| [FR-113](06-monitoring-and-operations.md)    | P0       | Monitoring and operations    |
| [FR-114](06-monitoring-and-operations.md)    | P0       | Monitoring and operations    |
| [FR-115](06-monitoring-and-operations.md)    | P0       | Monitoring and operations    |
| [FR-116](03-development.md)                  | P0       | Development                  |
| [FR-117](07-security.md)                     | P0       | Security                     |
| [FR-118](07-security.md)                     | P1       | Security                     |
| [FR-119](05-deployment.md)                   | P0       | Deployment                   |
| [FR-120](04-testing.md)                      | P0       | Testing                      |
| [FR-121](05-deployment.md)                   | P1       | Deployment                   |
| [FR-122](06-monitoring-and-operations.md)    | P1       | Monitoring and operations    |
| [FR-123](06-monitoring-and-operations.md)    | P1       | Monitoring and operations    |
| [FR-124](08-communication.md)                | P1       | Communication                |
| [FR-125](07-security.md)                     | P0       | Security                     |
| [FR-126](08-communication.md)                | P2       | Communication                |
| [FR-127](10-core-platform.md)                | P2       | Core platform                |
| [FR-128](06-monitoring-and-operations.md)    | P1       | Monitoring and operations    |
| [FR-129](05-deployment.md)                   | P1       | Deployment                   |
| [FR-130](05-deployment.md)                   | P0       | Deployment                   |
| [FR-131](10-core-platform.md)                | P0       | Core platform                |
| [FR-132](02-architecture-and-design.md)      | P0       | Architecture and design      |
| [FR-133](03-development.md)                  | P0       | Development                  |
| [FR-134](05-deployment.md)                   | P0       | Deployment                   |
| [FR-135](05-deployment.md)                   | P0       | Deployment                   |
| [FR-136](09-traceability-and-integration.md) | P0       | Traceability and integration |
| [FR-137](10-core-platform.md)                | P0       | Core platform                |
| [FR-138](10-core-platform.md)                | P0       | Core platform                |
| [FR-139](01-requirements.md)                 | P0       | Requirements                 |
| [NFR-001](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-002](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-003](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-004](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-005](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-006](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-007](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-008](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-009](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-010](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-011](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-012](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-013](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-014](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-015](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-016](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-017](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-018](12-non-functional.md)              | —        | Non-functional requirements  |
| [NFR-019](12-non-functional.md)              | —        | Non-functional requirements  |
