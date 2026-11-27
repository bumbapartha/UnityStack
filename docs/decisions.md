# Decisions

[← Documentation index](README.md)

All settled decisions in one place, accepted on 27 November 2026. To change one, edit its row in a PR. Undecided items are in [Open questions and risks](open-questions.md).

The ID prefix only says what kind of decision it is: **PD** = product (what UnityStack is and who it is for), **AD** = architecture (how it is built), **TD** = tool (which tool fills a capability). Requirements link to these IDs.

## Product decisions

| ID | Decision | What was decided | Why | Affects |
|---|---|---|---|---|
| <a id="pd-1"></a>**PD-1** | Primary user | The founder is the sole user and developer for the next 6–12 months, building UnityStack with UnityStack. Free demos with a few developers or organisations come later; enterprise buyers after that. | Early stage, one developer. Enterprise needs (installer, SSO, HA, backfill) stay as direction (P2). | [Vision and principles](vision.md), [Scope and roadmap](roadmap.md) |
| <a id="pd-2"></a>**PD-2** | Adapters everywhere | Every SDLC tool and the LLM are reached only through an adapter; the open-source tools are defaults and can be swapped (for example Jira, another model). | No lock-in: UnityStack is the fixed core, every tool around it can change. | [Core platform](requirements/10-core-platform.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="pd-3"></a>**PD-3** | Product core: the closed loop | Requirement → stories → code and tests → PR → deploy → logs → AI analysis → bug → next change, joined by one Change ID. AI drafting, traceability and the lifecycle are built together. | AI generates a lot quickly; end-to-end traceability keeps track of what was built for what. | [Architecture](architecture.md), [Traceability and integration](requirements/09-traceability-and-integration.md), [Monitoring and operations phase](requirements/06-monitoring-and-operations.md) |
| <a id="pd-4"></a>**PD-4** | Thin loop first | Build every stage in a basic form end to end, then deepen each stage. | Surfaces integration problems early and avoids rework; MVP = roadmap Phases 0–4. | [Scope and roadmap](roadmap.md) |
| <a id="pd-5"></a>**PD-5** | Open-source defaults | Every default provider is open source; swapping one means an adapter and configuration change only. Later, organisations pick a provider per capability at install time. | Organisations pay only for their own infrastructure (AWS, Azure, on-premises), not tool licences. See [NFR-015](requirements/12-non-functional.md). | [Tools and adapters](tools-and-adapters.md), [Distribution and onboarding](requirements/11-distribution-and-onboarding.md) |

## Architecture decisions

| ID | Decision | What was decided | Why | Affects |
|---|---|---|---|---|
| <a id="ad-1"></a>**AD-1** | Java only, portable AI module | AI stays in Java, in its own module with clean interfaces, plain data objects and no Spring-specific code. Replaces open question D-1 (Python service). | Java is the core language; one service to run. The module boundary keeps a later move to Python contained ([NFR-016](requirements/12-non-functional.md)). | [Core platform](requirements/10-core-platform.md), [Architecture](architecture.md) |
| <a id="ad-2"></a>**AD-2** | Docker first, then AWS | Everything runs locally in Docker Compose with profiles until the thin loop works, then moves to AWS (Terraform, EC2 + Compose) as soon as possible. Moving heavy tools to EC2 first is allowed. Replaces D-2. | Saves cost and setup until the loop is proven. | [Development setup](dev-setup.md), [Scope and roadmap](roadmap.md) |
| <a id="ad-3"></a>**AD-3** | Keycloak from the start | Keycloak handles sign-in, users, groups and broad roles; UnityStack checks project- and resource-level permissions. No temporary built-in login. | Avoids redoing users, roles and sessions later. | [Security](requirements/07-security.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="ad-4"></a>**AD-4** | Git provider behind an adapter | GitHub is the first Git adapter; Gitea, GitLab, Bitbucket and Azure DevOps Repos follow. "GitHub owns Git state" means the connected Git provider. | Organisations use different Git hosts; Gitea allows a fully open-source stack. | [Development phase](requirements/03-development.md), [Traceability and integration](requirements/09-traceability-and-integration.md), [Tools and adapters](tools-and-adapters.md) |

## Tool decisions

| ID | Decision | What was decided | Why | Affects |
|---|---|---|---|---|
| <a id="td-01"></a>**TD-01** | CI/CD | GitHub Actions behind the CI adapter; Jenkins and Argo CD later. Replaces D-6. | Most common choice; checks only while local (TD-10). | [Deployment phase](requirements/05-deployment.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-02"></a>**TD-02** | Security scanning | Semgrep (code), Trivy (dependencies, images, IaC, secrets), OWASP ZAP (black-box, after deploy). | Three different kinds of testing are needed; all open source. MCP servers are checked for maturity before use. | [Security](requirements/07-security.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-03"></a>**TD-03** | Testing | JUnit, Vitest, Playwright; Kiwi TCMS for test cases, runs and release reports. | Keeps a permanent, traceable record of what was tested and passed in each release. | [Testing phase](requirements/04-testing.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-04"></a>**TD-04** | Container registry | GitHub Container Registry, an ECR adapter with the AWS move, Harbor later. | Not needed while building locally; organisations choose at install time. | [Deployment phase](requirements/05-deployment.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-05"></a>**TD-05** | Observability | OpenTelemetry (Java agent and Collector) → Loki (logs), Prometheus (metrics), Grafana. Elasticsearch/Kibana or OpenSearch later as log adapters. Replaces D-3 and D-15. | Light enough for a laptop; Logback/Log4j keep working; swapping the log store changes Collector config and one adapter, not app code. | [Monitoring and operations phase](requirements/06-monitoring-and-operations.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-06"></a>**TD-06** | Alert rules | Grafana alerting; rules are files in Git, provisioned into Grafana; an export sync opens a PR on drift. Replaces D-14. | Alert changes are reviewed like everything else. | [Monitoring and operations phase](requirements/06-monitoring-and-operations.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-07"></a>**TD-07** | On-call | GoAlert for rotations, overrides, escalation and phone/SMS paging; AI adds an incident summary. | Open-source paging at night, with rotations editable at any time. | [Monitoring and operations phase](requirements/06-monitoring-and-operations.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-08"></a>**TD-08** | Feature flags | Code calls OpenFeature; Unleash is the flag server. Organisations can plug in their own provider. | OpenFeature is a ready-made adapter. | [Deployment phase](requirements/05-deployment.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-09"></a>**TD-09** | Notifications | Mattermost first through a notification adapter; Slack and Teams later. | Open source and runs in Docker. | [Communication](requirements/08-communication.md), [Tools and adapters](tools-and-adapters.md) |
| <a id="td-10"></a>**TD-10** | Local rebuilds | No CI deployment locally: AI runs `docker compose up --build -d <service>` after a batch of edits; `docker compose watch` for manual edits; CI still tests and scans each PR. On AWS later: preview environment per PR, main deploys to production. Replaces D-16. | Avoids constant restarts while AI edits files. | [Development setup](dev-setup.md), [Deployment phase](requirements/05-deployment.md) |
| <a id="td-11"></a>**TD-11** | AI meeting assistant | Planned as a future capability (P2); meeting platform still open. | Requirement meetings benefit from an AI participant that summarises and proposes updates. | [Communication](requirements/08-communication.md) |

## Resolved open questions

These former open questions were settled on 27 November 2026.

| Former ID | Decision |
|---|---|
| D-1 | Stay in Java; AI in its own framework-free module (AD-1). |
| D-2 | Local Docker until the thin loop works, then AWS as soon as possible (AD-2). |
| D-3 | Grafana, Loki and Prometheus for observability ([Tools and adapters](tools-and-adapters.md)). |
| D-6 | GitHub Actions for CI/CD; Jenkins and Argo CD as future adapters. |
| D-14 | Grafana alerting, with rules kept as files in Git (Git is the source) and an export sync as a safety net. |
| D-15 | OpenTelemetry (Java agent, Collector) for logs, metrics and traces; Loki first, Elasticsearch/Kibana or OpenSearch as later log adapters. |
| D-16 | No CI deployment locally: AI rebuilds with `docker compose up --build` after a batch of edits; `docker compose watch` for manual edits. Per-PR preview and main environments on AWS later. |

## Detailed ADRs still to write

| ADR | Subject |
|---|---|
| ADR-001 | Independent single-tenant installation per organisation |
| ADR-002 | GitHub canonical artifact ownership and review governance |
| ADR-003 | Cloud provisioning boundary (AWS / Terraform) |
| ADR-004 | Docker Compose runtime and evolution path |
| ADR-005 | AI in Java, in a portable framework-free module (AD-1) — decided |
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
| ADR-016 | Observability with Grafana, Loki and Prometheus, and log-to-Change-ID correlation ([D-4](open-questions.md)) |
| ADR-017 | **New.** Keycloak identity and the authorisation split with UnityStack (AD-3) |
| ADR-018 | **New.** Security scanning pipeline: Semgrep, Trivy, OWASP ZAP |
| ADR-019 | **New.** Test management and release test reports with Kiwi TCMS |
| ADR-020 | **New.** Alerting, on-call and AI incident summaries with GoAlert |
| ADR-021 | **New.** Local Compose profiles and the move to AWS (AD-2) |
