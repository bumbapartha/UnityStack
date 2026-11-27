# Document history

[← Documentation index](README.md)

## Versions

| Version | Date | Summary |
|---|---|---|
| 1.0–1.1 | Nov 2026 | Initial PRD; platform lifecycle, identity, ownership, registry, events, traceability additions (§11A–H, FR-048 to FR-060). |
| 1.2 | 21 Nov 2026 | AI-first two-pane UX, coexistence, Change ID, enterprise adoption (§17–21, FR-061 to FR-101). |
| 1.3 | Nov 2026 | Project Inception (§22, FR-102 to FR-108). |
| 2.0 | 27 Nov 2026 | Consolidated into one current-state document; product decisions PD-1 to PD-3; priorities reset; adapter architecture and closed loop made central; new FR-109 to FR-116 and NFR-013/014; observability added as a capability. |
| 2.1 | 27 Nov 2026 | PD-4 thin loop first, PD-5 open-source defaults with swappable adapters; AD-1 Java with portable AI module, AD-2 Docker then AWS, AD-3 Keycloak now; default tools for identity, CI/CD, registry, security scanning, testing, observability, on-call, feature flags and notifications; AI meeting assistant as a future capability; new FR-117 to FR-129 and NFR-015 to NFR-017; OpenTelemetry, Grafana alerting rules in Git and local rebuild workflow; §15.3 runtime environments; AD-4 Git provider adapter (GitHub first; Gitea, GitLab, Bitbucket, Azure DevOps later). |
| 2.2 | 27 Nov 2026 | Split into topic files under `docs/`: requirements grouped by SDLC phase with cross-links to tools and decisions; all decisions in one table; dogfooding plan merged into the roadmap. No requirement content changed. |

## Where PRD v2.1 sections went

| PRD v2.1 section | Now in |
|---|---|
| §1 About this document | [README.md](README.md) |
| §2 Product summary | [vision.md](vision.md) |
| §3 Settled product decisions (27 November 2026) | [decisions.md](decisions.md) |
| §3.1 Architecture decisions (27 November 2026) | [decisions.md](decisions.md) |
| §4 Product principles | [vision.md](vision.md) |
| §5 Users and roles | [vision.md](vision.md) |
| §6 Scope and releases | [roadmap.md](roadmap.md) |
| §7 The closed loop | [architecture.md](architecture.md) |
| §8.1 Capabilities and adapters | [requirements/02-architecture-and-design.md](requirements/02-architecture-and-design.md) |
| §8.2 LLM adapter and model-independent context | [requirements/02-architecture-and-design.md](requirements/02-architecture-and-design.md) |
| §8.3 Capabilities and default providers | [tools-and-adapters.md](tools-and-adapters.md) |
| §9 AI workspace model | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §10 Project Inception — AI-led project creation (built) | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §11 Source authority, sync and coexistence | [requirements/09-traceability-and-integration.md](requirements/09-traceability-and-integration.md) |
| §12 Traceability and Change ID | [requirements/09-traceability-and-integration.md](requirements/09-traceability-and-integration.md) |
| §13.1 Adapters and capability registry | [requirements/02-architecture-and-design.md](requirements/02-architecture-and-design.md) |
| §13.2 LLM adapter and AI control plane | [requirements/02-architecture-and-design.md](requirements/02-architecture-and-design.md) |
| §13.3 AI workspaces and revisions | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §13.4 Requirements and planning | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §13.5 Development, tests and Git lifecycle | [requirements/03-development.md](requirements/03-development.md) |
| §13.6 Delivery | [requirements/05-deployment.md](requirements/05-deployment.md) (FR-117/118 in [Security](requirements/07-security.md), FR-120/041 in [Testing](requirements/04-testing.md)) |
| §13.7 Observe, analyse and feedback | [requirements/06-monitoring-and-operations.md](requirements/06-monitoring-and-operations.md) (FR-124 in [Communication](requirements/08-communication.md)) |
| §13.8 Traceability and Change ID | [requirements/09-traceability-and-integration.md](requirements/09-traceability-and-integration.md) |
| §13.9 Coexistence, sync and ingestion | [requirements/09-traceability-and-integration.md](requirements/09-traceability-and-integration.md) |
| §13.10 Workflow orchestration | [requirements/09-traceability-and-integration.md](requirements/09-traceability-and-integration.md) |
| §13.11 Projects, repositories and Inception | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §13.12 Identity and security | [requirements/07-security.md](requirements/07-security.md) |
| §13.13 Design and documentation tools | [requirements/02-architecture-and-design.md](requirements/02-architecture-and-design.md) |
| §13.14 Distribution and platform operations (Later) | [requirements/10-platform-and-distribution.md](requirements/10-platform-and-distribution.md) |
| §13.15 Future: AI meeting assistant | [requirements/08-communication.md](requirements/08-communication.md) |
| §14 Security, identity and governance | [requirements/07-security.md](requirements/07-security.md) |
| §15.1 Current implementation (as of 27 November 2026) | [architecture.md](architecture.md) |
| §15.2 Target logical architecture | [architecture.md](architecture.md) |
| §15.3 Runtime environments (AD-2) | [dev-setup.md](dev-setup.md) |
| §15.4 Artifact types and repository flexibility | [architecture.md](architecture.md) |
| §15.5 Later: enterprise distribution and onboarding (P2) | [requirements/10-platform-and-distribution.md](requirements/10-platform-and-distribution.md) |
| §16 Non-functional requirements | [requirements/11-non-functional.md](requirements/11-non-functional.md) |
| §17 MVP acceptance — the thin loop | [roadmap.md](roadmap.md) |
| §18.1 Resolved on 27 November 2026 | [decisions.md](decisions.md) |
| §18.2 Still open | [open-questions.md](open-questions.md) |
| §19 Risks | [open-questions.md](open-questions.md) |
| Appendix A — Change history and section map | [history.md](#) |
| Appendix B — Architecture decision records to prepare | [decisions.md](decisions.md) |
| Appendix C — Next documents | [README.md](README.md) |

## Where dogfooding plan sections went

Plan §2 (product principles) duplicated the PRD principles and is covered by the vision file. Plan §13 (decision register) is replaced by the decisions table.

| Dogfooding plan section | Now in |
|---|---|
| §1 Executive idea | [roadmap.md](roadmap.md) |
| §2 Product principles | [vision.md](vision.md) |
| §3 Dogfooding loop | [roadmap.md](roadmap.md) |
| §4 Delivery architecture (initial target) | [architecture.md](architecture.md) |
| §5 Workspace and artifact contract | [requirements/01-requirements.md](requirements/01-requirements.md) |
| §6 Phased implementation plan | [roadmap.md](roadmap.md) |
| §7 MVP scope boundaries | [roadmap.md](roadmap.md) |
| §8 Git and repository organisation | [architecture.md](architecture.md) |
| §9 Work items and prioritisation | [roadmap.md](roadmap.md) |
| §10 Data, provenance and safety rules | [requirements/07-security.md](requirements/07-security.md) |
| §11 Measuring success | [roadmap.md](roadmap.md) |
| §12 Risks and mitigation | [open-questions.md](open-questions.md) |
| §13 Decision register | [decisions.md](decisions.md) |
| §14 First sprint: definition of done | [roadmap.md](roadmap.md) |

## Where v1.x sections went in v2.0

This table refers to v1.x and v2.0 section numbers.

| Old section | New section |
|---|---|
| §1 Executive summary, §2 Objectives | §2, §4 |
| §3 Scope, §21 Updated MVP | §6, §17 |
| §4 Stakeholders | §5 |
| §5 Tool stack | §8.3 |
| §6.1–6.6, §6.7 Functional requirements | §13 |
| §7 Artifact types | §15.4 |
| §8 Reference architecture | §15.2 |
| §9 AWS deployment | §15.3, §15.5 |
| §10 Governance, §11B Identity | §14 |
| §11 NFRs | §16 |
| §11A Platform lifecycle | §15.5 |
| §11C Ownership and reconciliation, §18 Coexistence | §11 |
| §11D Capability registry | §8.1 |
| §11E Workflow events | §11, §18 (D-11) |
| §11F Traceability, §19 Orchestration | §12 |
| §11G Acceptance, §16 Success criteria | §17 |
| §11H, §21.1 ADRs | Appendix B |
| §12 User journeys | §7, §17 |
| §13 Milestones | §6 |
| §14 Risks | §19 |
| §15, §21.2 Open decisions | §18 |
| §17 AI-first interaction | §9 |
| §20 Enterprise adoption | §15.5 |
| §22 Project Inception | §10 |
