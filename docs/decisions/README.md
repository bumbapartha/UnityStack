# Decisions

[← Documentation index](../README.md)

One short file per settled decision. To change a decision, edit its file in a PR and say what it replaces. Undecided items are in [Open questions and risks](../open-questions.md).

## Product decisions

| ID | Decision |
|---|---|
| [PD-1](PD-1-primary-user.md) | Primary user: the founder, dogfooding |
| [PD-2](PD-2-adapters-everywhere.md) | Adapters everywhere |
| [PD-3](PD-3-closed-loop.md) | Product core: the closed loop joined by one Change ID |
| [PD-4](PD-4-thin-loop-first.md) | Thin loop first |
| [PD-5](PD-5-open-source-defaults.md) | Open-source defaults, swappable by adapter |

## Architecture decisions

| ID | Decision |
|---|---|
| [AD-1](AD-1-java-portable-ai-module.md) | Java only, with a portable AI module |
| [AD-2](AD-2-docker-then-aws.md) | Local Docker until the thin loop works, then AWS |
| [AD-3](AD-3-keycloak-now.md) | Keycloak from the start |
| [AD-4](AD-4-git-provider-adapter.md) | Git provider behind an adapter |

## Tool decisions

| ID | Decision |
|---|---|
| [TD-01](TD-01-ci-cd-github-actions.md) | CI/CD: GitHub Actions |
| [TD-02](TD-02-security-scanning.md) | Security scanning: Semgrep, Trivy, OWASP ZAP |
| [TD-03](TD-03-testing-tools.md) | Testing: JUnit, Vitest, Playwright, Kiwi TCMS |
| [TD-04](TD-04-container-registry.md) | Container registry: GitHub Container Registry, then ECR |
| [TD-05](TD-05-observability.md) | Observability: OpenTelemetry with Grafana, Loki and Prometheus |
| [TD-06](TD-06-alert-rules-in-git.md) | Alert rules: Grafana alerting, rules kept in Git |
| [TD-07](TD-07-on-call-goalert.md) | On-call: GoAlert |
| [TD-08](TD-08-feature-flags.md) | Feature flags: OpenFeature with Unleash |
| [TD-09](TD-09-notifications.md) | Notifications: Mattermost |
| [TD-10](TD-10-local-rebuild-workflow.md) | Local rebuilds, no CI deployment while local |
| [TD-11](TD-11-meeting-assistant-later.md) | AI meeting assistant as a future capability |

## Resolved open questions

| Former ID | Decision |
|---|---|
| D-1 | Stay in Java; AI in its own framework-free module ([AD-1](AD-1-java-portable-ai-module.md)). |
| D-2 | Local Docker until the thin loop works, then AWS as soon as possible ([AD-2](AD-2-docker-then-aws.md)). |
| D-3 | Grafana, Loki and Prometheus for observability ([Tools and adapters](../tools-and-adapters.md)). |
| D-6 | GitHub Actions for CI/CD; Jenkins and Argo CD as future adapters. |
| D-14 | Grafana alerting, with rules kept as files in Git (Git is the source) and an export sync as a safety net. |
| D-15 | OpenTelemetry (Java agent, Collector) for logs, metrics and traces; Loki first, Elasticsearch/Kibana or OpenSearch as later log adapters. |
| D-16 | No CI deployment locally: AI rebuilds with `docker compose up --build` after a batch of edits; `docker compose watch` for manual edits. Per-PR preview and main environments on AWS later. |
| — | Thin loop first ([PD-4](PD-4-thin-loop-first.md)); Git provider adapter with GitHub first ([AD-4](AD-4-git-provider-adapter.md)); Keycloak now ([AD-3](AD-3-keycloak-now.md)); Semgrep, Trivy, OWASP ZAP; JUnit, Vitest, Playwright, Kiwi TCMS; GitHub Container Registry (ECR with AWS, Harbor later); GoAlert; OpenFeature with Unleash; Mattermost; AI meeting assistant as a future feature. |

## Detailed ADRs still to write

| ADR | Subject |
|---|---|
| ADR-001 | Independent single-tenant installation per organisation |
| ADR-002 | GitHub canonical artifact ownership and review governance |
| ADR-003 | Cloud provisioning boundary (AWS / Terraform) |
| ADR-004 | Docker Compose runtime and evolution path |
| ADR-005 | AI in Java, in a portable framework-free module ([AD-1](AD-1-java-portable-ai-module.md)) — decided |
| ADR-006 | Model-agnostic LLM adapter and model-independent context |
| ADR-007 | Capability adapters and registry |
| ADR-008 | Git synchronisation, conflict resolution and native-tool ownership |
| ADR-009 | Identity, permissions, trusted execution and agent security |
| ADR-010 | Workflow event contract, retries and recovery |
| ADR-011 | GitHub as authority for repository/PR state; UnityStack for orchestration state |
| ADR-012 | AI-first two-pane workspaces and read-only draft revision model |
| ADR-013 | Source-system authority and passive ingestion |
| ADR-014 | Enterprise onboarding, backfill and provenance |
| ADR-015 | Walking-skeleton delivery and Change ID traceability |
| ADR-016 | Observability with Grafana, Loki and Prometheus, and log-to-Change-ID correlation ([D-4](../open-questions.md)) |
| ADR-017 | **New.** Keycloak identity and the authorisation split with UnityStack ([AD-3](AD-3-keycloak-now.md)) |
| ADR-018 | **New.** Security scanning pipeline: Semgrep, Trivy, OWASP ZAP |
| ADR-019 | **New.** Test management and release test reports with Kiwi TCMS |
| ADR-020 | **New.** Alerting, on-call and AI incident summaries with GoAlert |
| ADR-021 | **New.** Local Compose profiles and the move to AWS ([AD-2](AD-2-docker-then-aws.md)) |
