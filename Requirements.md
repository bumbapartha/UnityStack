# UnityStack — Product Requirements Document

**Product:** UnityStack — Enterprise AI Software Factory  
**Document type:** Product Requirements Document (PRD), single current-state version  
**Version:** 2.0 (consolidated draft)  
**Status:** Draft for review  
**Date:** 27 November 2026  
**Owner:** Partha Sarathi Ghosh  

---

## 1. About this document

This version replaces the stacked v1.1, v1.2 and v1.3 drafts with one current-state document. Earlier versions remain in Git history; Appendix A maps old sections to new ones.

- **Requirement IDs are stable.** Every FR-001 to FR-108 ID from earlier versions is kept, so existing references stay valid. New requirements start at FR-109. IDs are grouped by area in §13 rather than by the version that introduced them.
- **Priorities have been reset** to match the product decisions in §3 (see §6 for what P0, P1 and P2 now mean).
- **Detailed design** (C4 diagrams, API schemas, Terraform modules, threat models) belongs in separate HLD and ADR documents, linked from Git.
- Inception (§10) is the only area currently specified at the slice level. Other areas will gain slice-level detail as they are built.

## 2. Product summary

UnityStack is an AI-first, Git-driven software delivery platform. It covers the whole lifecycle — requirements, planning, code and tests, review, deployment, monitoring and feedback — and links every step with one shared change identifier.

People work with AI through a two-pane workspace: a conversation on the left and a live, read-only preview of the artifact on the right. The AI proposes; a person finalises; GitHub pull requests and policy govern what becomes approved. Teams can also work entirely in their own tools, and UnityStack observes and links that work without blocking it.

Every external tool, and the AI model itself, sits behind an adapter, so any of them can be swapped without changing the product.

**Guiding statement:** *From requirements to production. Powered by AI. Governed by humans.*

## 3. Settled product decisions (27 November 2026)

| # | Question | Decision |
|---|---|---|
| PD-1 | Primary user for the next 6–12 months | **The founder as sole user and developer, dogfooding.** UnityStack is built with UnityStack. Later, free demos with a few developers or organisations; enterprise buyers after that. Enterprise needs (installer, SSO, HA, onboarding, backfill) stay as direction, not current design constraints. |
| PD-2 | Bundle or connect | **Adapters everywhere.** Every SDLC tool and the LLM are reached only through an adapter. The nine selected open-source tools (§8.3) are the default providers, chosen because they are open source; alternatives such as Jira, or a different model, must be swappable. Bundling all nine into an installer is a later packaging concern, not the product core. |
| PD-3 | Product core | **The closed loop, joined by one shared ID.** Requirement → stories → code and tests on a branch → PR review → deployment → logs and monitoring → AI-assisted problem detection → accepted bug back in the tracker → next change. AI drafting, traceability and the full lifecycle go hand in hand; none is built in isolation. |

**Consequence for delivery (proposed, to confirm):** build the loop *thin* from end to end first, with the Change ID in place from day one, then deepen each stage. See §6 and §17.

## 4. Product principles

1. **AI-first, not AI-only.** UnityStack-native artifacts are created and revised through AI conversation; there are no manual authoring forms in the MVP. Editing in external tools, IDEs and GitHub is always allowed.
2. **AI proposes, humans govern.** The user explicitly finalises every AI proposal. GitHub branch protection, required checks and policy decide what merges and deploys. A successful AI response or test run is never a substitute for required human approval.
3. **GitHub owns Git state.** GitHub is authoritative for repositories, branches, commits, PRs, reviews and merged artifacts. Each connected tool is authoritative for its own native records. UnityStack owns conversations, drafts, orchestration state, indexes and trace links.
4. **Adapters everywhere.** Workflows and agents call logical capabilities (for example `projectManagement.createStory`), never a provider's API directly. The LLM is a capability like any other.
5. **Your data stays yours.** Context lives in UnityStack's store, Git and the connected tools — never only inside a model provider. Switching model means the new model is fed the same context from those stores.
6. **One ID through the loop.** A stable Change ID links requirement, story, ADR, branch, commit, PR, test, build, deployment, log evidence and bug.
7. **Traditional SDLC remains valid.** Work done entirely outside UnityStack is observed and linked, never blocked.
8. **Walking skeleton and continuous delivery.** A deployable Hello World comes first; everything grows through small governed changes.
9. **Everything durable as code.** Requirements, ADRs, diagrams, API specs, code, tests, pipelines and infrastructure are Git artifacts. Runtime state, logs, metrics, secrets and tool databases are not.
10. **One reusable workspace, many modules.** Chat, preview, diff, revision, finalise and PR mechanics are shared; agents and renderers vary by role.

## 5. Users and roles

**Now:** one person (the founder) plays every role below, using UnityStack to build UnityStack.

**Later:** each role can be a different person, and an organisation can require that the reviewer is not the author.

| Role | Responsibility |
|---|---|
| Organisation administrator | Configure UnityStack, connected tools, policies and access |
| Product owner / business analyst | Shape requirements, epics, stories and priorities |
| UX designer | Review wireframes and UX artifacts |
| Solution architect | Review architecture, ADRs and API contracts |
| Developer | Discuss requirements, review and implement code and tests |
| Reviewer | Review and approve PRs |
| QA engineer | Review tests and quality evidence |
| DevOps / SRE | Manage pipelines, environments, releases, monitoring and incidents |
| AI agent | Propose artifacts and actions only within granted permissions |

## 6. Scope and releases

Priorities in this document mean:

| Priority | Meaning |
|---|---|
| **P0 — MVP** | Needed for the thin closed loop, used by the sole user (PD-1, PD-3). |
| **P1 — Next** | Deepens the loop while dogfooding and prepares for a small group of demo users. |
| **P2 — Later** | Enterprise distribution and adoption: installer, full nine-tool bundle, BYOT selection, SSO, HA, large-scale ingestion. |

### 6.1 MVP (P0) — the thin closed loop

- Project Inception (§10) — **built**.
- Requirements workspace: AI edits a Markdown requirements document read from GitHub, with preview, diff, revisions, finalise → PR.
- Planning: AI decomposes approved requirements into epics and stories; after merge they are published to the project-management tool through its adapter.
- Development: AI proposes code **and tests together** for a story, on a branch, as a PR that carries the Change ID.
- Review and merge through GitHub, with UnityStack reflecting PR status.
- Delivery: CI builds, tests and deploys the managed application (starting with Hello World) to one environment.
- Observe: the deployed application's logs are collected and viewable; the user can ask AI to analyse them.
- Feedback: AI can propose a bug from log evidence; once the user accepts it, the bug is created in the tracker, linked to the Change ID, and enters the loop again.
- Adapters for every tool used above, plus the LLM adapter with a mock provider.
- Change ID created at the start and carried through every stage.

### 6.2 Next (P1)

- Second provider behind an existing adapter (Jira for project management) to prove swappability.
- Architecture workspace (HLD, ADRs, Mermaid) and richer previews.
- Passive observation of work done outside UnityStack, webhook reconciliation and bounded history import.
- My Work queues, workflow orchestration with parallel work and dependencies.
- Basic multi-user sign-in and roles, ready for demo users.
- Model routing, budgets, RAG over project documents, agent evaluation.

### 6.3 Later (P2)

- Versioned, installable distribution with Terraform-provisioned AWS, the full nine-tool profile, upgrade, backup and restore.
- Bring Your Own Tool chosen by the customer at install time or runtime.
- Enterprise onboarding: connect existing tools, historical backfill, relationship reconstruction, completeness reports.
- Full SSO, HA, Kubernetes, multi-cloud, cross-repository change sets.

### 6.4 Explicit exclusions from the MVP

Manual artifact editors inside UnityStack; installing all nine tools; enterprise installer; universal SSO; high availability; multi-tenant SaaS; multi-cloud; autonomous production deployment; atomic cross-repository merges; advanced visual diagram diffs; large-scale historical ingestion.

## 7. The closed loop

```mermaid
flowchart LR
  I[Inception] --> R[Requirements]
  R --> P[Epics and stories]
  P --> D[Code + tests on a branch]
  D --> PR[PR review and merge]
  PR --> CI[Build, test, deploy]
  CI --> O[Logs and monitoring]
  O --> A[AI analysis]
  A -->|user accepts| B[Bug in tracker]
  B --> D
  B -.-> R
```

| Stage | What happens | Human control point | Authority |
|---|---|---|---|
| Inception | AI captures the project charter (§10) | User confirms the charter | UnityStack, then Git once a repository is linked |
| Requirements | AI drafts and revises requirement documents | User finalises a revision → PR | Git |
| Planning | AI proposes epics, stories and acceptance criteria | User finalises → PR; after merge, published to the tracker | Git for the approved definition; tracker for operational status |
| Development | AI proposes code and tests together for a story | User finalises → PR | Git |
| Review | PR reviewed and merged under repository policy | Reviewer / policy | GitHub |
| Delivery | CI builds, tests and deploys | Environment protection and release policy | CI/CD system |
| Observe | Logs and metrics collected, tagged with service version and Change ID | — | Monitoring system |
| Analyse | User asks AI about behaviour in an environment; AI answers with log evidence | — | UnityStack (analysis is derived, not authoritative) |
| Feedback | AI proposes a bug with evidence | User accepts → bug created in tracker | Tracker |

**Rules:** each stage can be done outside UnityStack and still be linked (§11). AI never advances a stage on its own: transitions come from user actions, policy and verified source events.

## 8. Adapter architecture

### 8.1 Capabilities and adapters

- Workflows and agents call **logical operations** on a capability, for example `projectManagement.createStory`, `scm.openPullRequest`, `ci.getRunStatus`, `observability.queryLogs`, `llm.complete`.
- An **adapter** translates those operations to one provider's API. Provisioning a tool and connecting to an existing instance are separate adapter concerns.
- A **capability registry** records, for each capability in each installation: provider ID, connection mode (provisioned or existing), endpoint, credential reference, supported operations, health and scope.
- Unsupported or edition-restricted operations return an explicit "unsupported operation" error; callers must handle it.
- MVP needs one working provider per capability used in the loop. The interfaces must not leak provider-specific concepts, so that a second provider can be added without changing workflows.

### 8.2 LLM adapter and model-independent context

- The LLM is reached only through a provider-neutral interface (today: `InceptionAgent` with mock and OpenAI implementations). Provider and model are chosen by configuration.
- A **mock provider** is always available for offline development and automated tests.
- All context an agent needs — conversation, charter, drafts, approved artifacts, decisions, trace links — is stored in UnityStack, Git or the connected tools. Nothing depends on provider-side memory, threads or assistants.
- Switching provider or model takes effect for the next agent turn. The new model is given context rebuilt from those stores, so in-progress work continues.
- Every agent run records the provider and model used (`agentRef`, for example `openai:gpt-4.1-mini`) for audit.

### 8.3 Capabilities and default providers

| Capability | Default (open source) | Alternatives to support later | Needed for MVP loop |
|---|---|---|---|
| Source control and PRs | GitHub | Other Git providers (P2) | Yes |
| LLM | Configurable (mock, OpenAI) | Any provider via the adapter; routing gateway such as LiteLLM (P1) | Yes |
| Project management (epics, stories, bugs) | OpenProject | Jira (P1), Plane (P2) | Yes |
| CI/CD | GitHub Actions | Jenkins and others (P2) | Yes |
| Observability (logs, metrics) | *To be selected — see D-3* | — | Yes |
| Architecture diagrams | Mermaid (library) | — | P1 |
| Technical documentation | MkDocs | Confluence (P2) | P1 |
| API documentation | Swagger UI | — | P1 |
| Code quality | SonarQube Community Build | — | P1 |
| Initial wireframes | Excalidraw | — | P2 |
| UX/UI design | Penpot | — | P2 |
| Software catalogue | Backstage | — | P2 |
| Web IDE | Eclipse Theia | Local IDE (always) | P2 |

The nine originally selected tools are OpenProject, Excalidraw, Penpot, Mermaid, MkDocs, Backstage, Swagger UI, Eclipse Theia and SonarQube Community Build. None of them covers logs and monitoring, so the loop needs an additional observability provider.

**Important:** an available provider does not imply full integration, SSO or feature parity. Each adapter documents its supported operations, versions, licensing and limitations.

## 9. AI workspace model

- **Two panes:** left, the AI conversation (questions, clarifications, progress, references); right, a continuously refreshed read-only preview. Narrow screens may stack the panes without losing either.
- **Role-specific previews:** requirements → BRD/PRD Markdown; planning → epic/story hierarchy; architecture → HLD, ADRs, rendered Mermaid and source diff; development → changed files and code diff with build/test feedback; testing → test cases and results; delivery → build, release and deployment state; observe → log excerpts and AI findings.
- **Revisions:** every AI proposal creates a draft revision with an immutable ID, timestamp, actor, agent-run reference, source baseline (Git commit SHA where applicable) and parent revision. The preview offers current, diff and history views. An accepted draft is never overwritten silently.
- **Draft lifecycle:** `OPEN → PROPOSED → USER_ACCEPTED → PR_OPEN → MERGED`, or `REVISE / REJECT / CLOSED`. PR and merge states are read from GitHub, not inferred from chat.
- **Finalisation:** the user explicitly accepts a specific revision; trusted code validates schema, permissions and destination before creating the branch, commit and PR. Finalising does not merge.
- **Conflicts:** if the upstream baseline changed since the draft started, show the conflict and offer rebase or restart; never overwrite.
- **Manual editing:** none inside UnityStack in the MVP. External editors, IDEs and GitHub remain available. An "open in editor" action is P2.

## 10. Project Inception — AI-led project creation (built)

**Purpose:** capture a new project's very high-level intent through a short AI conversation instead of a form. Inception is deliberately simple; features, modules and detailed requirements are discovered later in the requirements and planning workspaces.

### 10.1 User journey

1. A signed-in user clicks **New Project**.
2. UnityStack opens the two-pane workspace: AI conversation on the left, live read-only **Project Charter** preview on the right.
3. The AI asks adaptive questions, one (or two closely related) at a time, skipping anything already answered.
4. When all mandatory items are captured, the workspace shows **Ready to create**; the AI presents a summary for confirmation.
5. The user explicitly confirms; UnityStack creates the project and offers next steps: connect or create a GitHub repository and start the requirements workspace.
6. If the user leaves before confirming, the inception conversation is discarded and nothing is created.

### 10.2 Information captured

| Item | Mandatory | Example prompt |
|---|---|---|
| Project name | Yes | "What should we call this project?" |
| Problem statement | Yes | "What problem will it solve?" |
| Target users | Yes | "Who will use it?" |
| Product type | Yes | "Is it a website, mobile app, API/backend, desktop app, internal tool, data/AI product, or something else?" (with follow-ups, e.g. iOS/Android/both) |
| Expected benefit | Yes | "Once this exists, what will be better for its users or the business?" (no metrics needed) |
| Scale | Yes | "How big is this: a personal or team tool, one department, the whole organisation, or public?" (one of: personal, team, department, organisation, public) |
| Why now / motivation | No | "Why is this needed now?" |
| Constraints | No | Deadline, budget, compliance, technology preferences, systems it must connect to |
| Out of scope | No | "Is there anything this project should definitely not do?" |

- Target length: about 8–10 questions; the user may create the project as soon as mandatory items are covered.
- "I don't know" is a valid answer and is recorded as an open question.
- The AI does **not** propose features or modules at inception; it records only what the user provides.
- Measurable success targets (KPIs) are **not** asked at inception, because they are hard to define this early. They are captured later in the requirements workspace, building on the expected benefit.

### 10.3 Data and ownership

- On confirmation, UnityStack stores in PostgreSQL: the project record (ID, short key, name, status, product type, creator, timestamps), the charter as structured data plus rendered Markdown, open questions, and the inception conversation with agent-run reference.
- The charter is versioned: each change creates a new revision with parent revision, actor and agent-run reference.
- Until a GitHub repository is linked, PostgreSQL is authoritative for the charter. When a repository is linked or created, UnityStack proposes the current charter to the mapped Git path through a PR; after merge, Git is authoritative and later changes follow the normal draft → finalise → PR flow.
- After creation, charter content changes through AI conversation. Project name, key and status may also be changed in a simple project settings panel; those changes are audited.

The requirements for this slice are FR-102 to FR-108 in §13.11.

**Acceptance:** a user creates a project by answering only the mandatory questions; the project and charter revision 1 exist in PostgreSQL; abandoning a second inception leaves no project; renaming the project creates an audit record.

## 11. Source authority, sync and coexistence

- **UnityStack is never a mandatory gateway.** A team can create stories in the tracker, code in a local IDE, open and merge PRs in GitHub, run CI/CD and deploy without opening UnityStack.
- **Authority:** GitHub owns repository, branch, commit, PR, review and merge truth. The tracker owns ticket and status records. CI/CD owns build and deployment execution. The monitoring system owns logs and metrics. UnityStack owns orchestration state, AI drafts, indexes, correlations and inferred links.
- **Artifact mapping record:** stable artifact ID, type, owning repository/path/branch, canonical format, originating tool, sync direction, last synced commit, tool revision and sync status.
- **Canonical lifecycle:** Draft → Validate → Proposed PR → Review → Merge → Synchronise connected tools → Verify.
- **Field-level ownership:** native tool edits are allowed. Operational fields (for example tracker status) belong to the tool; Git-approved fields (for example acceptance criteria) belong to Git. Drift is reported, never silently overwritten; conflicts show both versions and a resolution path.
- **Ingestion:** webhooks where available, API polling, checkpoints and scheduled reconciliation. Duplicate and out-of-order events are handled idempotently. Ingesting never modifies the source record.
- **Unlinked work:** PRs or stories without a matching Change ID are kept and shown; UnityStack may suggest links but never fabricates them or blocks the work.
- **Idempotency:** idempotency keys and optimistic revision checks prevent duplicate PRs, duplicate tracker items and replayed updates. Failed publication is visible, retryable and audited, and an unmerged proposal is never treated as approved.
- **Limits:** UnityStack cannot reconstruct records deleted before ingestion, outside granted access, not exposed by APIs or expired under the source's retention. It promises best-effort capture of accessible data and transparent gap reporting, not zero data loss.

## 12. Traceability and Change ID

- Every UnityStack-initiated change gets a stable **Change ID** at the start of the loop (format to be decided — see D-4; for example `<PROJECTKEY>-CHG-001`).
- The Change ID is carried through: requirement and story IDs, branch name, commit trailer, PR title or label, CI run metadata, build and image version, deployment record, structured log fields, and bug records.
- External work keeps its own source IDs (tracker key, PR number, commit SHA) even when no Change ID exists yet.
- **Verified links** come from explicit references (an ID in a branch, commit, PR or ticket). **Inferred links** proposed by AI are labelled with confidence and evidence and stay unverified until a person confirms them.
- Runtime tracing IDs (for example OpenTelemetry `trace_id`) are distinct from the Change ID and are correlated through service version and deployment metadata.
- Goal: one end-to-end timeline per Change ID, reproducible from stored provenance, with missing links shown as gaps rather than filled in.

## 13. Functional requirements

IDs are stable (see §1). "Was" shows the earlier priority where it changed.

### 13.1 Adapters and capability registry

| ID | Priority | Requirement |
|---|---|---|
| FR-109 | P0 | **New.** Every external tool is accessed only through a capability adapter; workflows and agents contain no provider-specific API calls. |
| FR-042 | P0 | Provide a capability registry and stable provider-independent interfaces for every capability used in the MVP loop. |
| FR-050 | P0 | The registry maps logical operations to installed or connected provider adapters, with endpoint, credential reference, supported operations and health. |
| FR-005 | P0 | Show each configured tool in the UnityStack portal with endpoint, connection mode and health. *(Was: show all nine installed tools.)* |
| FR-112 | P1 | **New.** Add Jira as a second project-management provider behind the same adapter, proving that the provider can be swapped without workflow changes. |
| FR-043 | P2 | Allow each capability to use a UnityStack-provisioned default, an existing customer instance, or a supported alternative provider, chosen by the customer. |
| FR-060 | P2 | Allow users to select an alternative provider, connect an existing enterprise instance, or provision a supported alternative per capability (BYOT) through the UI. |

### 13.2 LLM adapter and AI control plane

| ID | Priority | Requirement |
|---|---|---|
| FR-026 | P0 | Provide an LLM adapter that switches model provider and model by configuration, with no workflow changes. |
| FR-110 | P0 | **New.** Always provide a mock LLM provider for offline development and automated tests. |
| FR-111 | P0 | **New.** Keep all agent context in UnityStack, Git or connected tools; after a provider or model switch, the next agent turn receives context rebuilt from those stores, with no dependency on provider-side memory. |
| FR-027 | P0 | Provide an AI business-analyst workflow that turns conversation into structured requirements-as-code. |
| FR-028 | P0 | Separate AI reasoning and requested actions from trusted identity, authorisation, validation, approval and tool execution. |
| FR-029 | P0 | Enforce configured reviewer approval for merges (review may be optional where permitted), mandatory policy and quality gates, and separately configured production deployment authorisation. User finalisation of an AI proposal is always explicit. |
| FR-030 | P0 | Record agent execution state, provider and model, failures, retries and tool-call audit events. |
| FR-031 | P0 | Add role agents progressively: requirements, planning, developer (code and tests), and observe/analyse agents are needed for the MVP loop; UX, architecture and operations agents follow. *(Was P1.)* |
| FR-032 | P1 | Introduce MCP-based tool integration where it adds value, with controlled permissions and validation. |
| FR-033 | P1 | Add RAG over authorised project documents, agent evaluation, and model cost and latency tracing. |
| FR-034 | P1 | Enforce retry limits, budgets, timeouts and human escalation for failed or ambiguous agent actions. |

### 13.3 AI workspaces and revisions

| ID | Priority | Requirement |
|---|---|---|
| FR-071 | P0 | Use AI conversation and a read-only live preview for all UnityStack-native artifact creation. |
| FR-093 | P0 | Provide role-specific two-pane workspaces on a common preview and revision framework. |
| FR-094 | P0 | Record revision history and show differences for each supported MVP artifact type. |
| FR-020 | P0 | Show the proposed artifact and diff before publication or approval. |
| FR-064 | P0 | Apply conversational revisions to versioned previews and show inline or structured diffs. |
| FR-065 | P0 | Require explicit user finalisation of a specific draft revision before creating a GitHub PR. |
| FR-077 | P2 | Offer launch-in-browser or local editor for manual artifact changes. |
| FR-078 | P2 | Optionally support direct artifact editing inside UnityStack. |

### 13.4 Requirements and planning

| ID | Priority | Requirement |
|---|---|---|
| FR-061 | P0 | Support conversational discovery and clarification of business requirements. |
| FR-062 | P0 | AI recommends story count, epic grouping, acceptance criteria and dependencies based on scope. |
| FR-063 | P0 | Show hierarchical epic/story and partial or full BRD previews. |
| FR-068 | P0 | After merge, publish approved epics and stories to the project-management tool through its adapter. *(Was: to OpenProject.)* |
| FR-069 | P0 | Make publication retryable and idempotent; no duplicate stories after retries or outages. |
| FR-070 | P0 | Show proposal → PR → merge → tool publication status and failure reasons. |
| FR-036 | P0 | Integrate the project-management adapter enough to link requirements and work items with Git-backed artifacts; document sync ownership and conflict handling. |

### 13.5 Development, tests and Git lifecycle

| ID | Priority | Requirement |
|---|---|---|
| FR-116 | P0 | **New.** For a story, the developer agent proposes code changes and matching tests together, on a branch, after reading the linked requirement, story, ADRs and repository conventions. |
| FR-018 | P0 | Treat approved GitHub content as the canonical version of durable, reviewable SDLC artifacts. |
| FR-019 | P0 | AI-generated changes are made on branches and proposed through PRs; no direct AI push to protected branches. |
| FR-021 | P0 | Validate generated artifacts against schemas and project rules before creating a PR. |
| FR-022 | P0 | Preserve actor, prompt/run reference, source requirement, artifact version, PR URL and approval status for audit. |
| FR-066 | P1 | Support configurable optional additional reviewer approval per organisation or repository. *(Sole user for now; see D-7.)* |
| FR-067 | P1 | Permit automatic merge only where policy and branch protection allow and all required checks pass. *(Was P0.)* |
| FR-024 | P1 | Detect and reconcile changes made directly in connected tools to prevent uncontrolled divergence from Git. |
| FR-025 | P2 | Coordinate multi-repository changes without assuming atomic cross-repository merges. |
| FR-017 | P2 | Track related PRs across repositories as a logical change set with dependencies and merge status. *(Was P1.)* |
| FR-058 | P2 | Support dependency-aware multi-repository change sets with explicit handling of partial merges. *(Was P1.)* |

### 13.6 Delivery

| ID | Priority | Requirement |
|---|---|---|
| FR-097 | P0 | Deploy a Hello World walking skeleton through an automated pipeline to a production-designated environment. |
| FR-044 | P0 | Version tests, CI/CD pipelines, deployment configuration and monitoring configuration in Git. *(Was P1.)* |
| FR-045 | P0 | Run validation and deployment through trusted pipelines with environment protection. *(Was P1.)* |
| FR-098 | P1 | Support configured automatic delivery or human production release approval with quality gates. |
| FR-041 | P1 | Bring code-quality results (SonarQube) and IDE workflows into PR review. |

### 13.7 Observe, analyse and feedback

| ID | Priority | Requirement |
|---|---|---|
| FR-113 | P0 | **New.** The deployed application emits structured logs (and basic metrics) tagged with service version and Change ID; they are collected by the observability provider and viewable from UnityStack. |
| FR-114 | P0 | **New.** The user can ask AI to analyse logs for an environment and time window; the AI's findings cite the log entries they are based on. |
| FR-115 | P0 | **New.** AI may propose a bug from its findings; the bug is created in the tracker only after a person accepts it, and is linked to the Change ID, deployment and log evidence. |
| FR-046 | P0 | Connect production signals to incident or improvement proposals that re-enter the governed loop. *(Was P1.)* |

### 13.8 Traceability and Change ID

| ID | Priority | Requirement |
|---|---|---|
| FR-099 | P0 | Assign persistent Change IDs and propagate them through artifacts, branches, commits, PRs, builds, deployments, logs and bugs. |
| FR-053 | P0 | Capture stable artifact IDs, Git commit/ref, source request and agent-run linkage for all generated artifacts. |
| FR-023 | P0 | Maintain traceability across requirement → design → code → tests → deployment → production feedback (thin, explicit links only). *(Was P1.)* |
| FR-056 | P0 | Support traceability links among requirements, architecture, APIs, implementation, tests, releases and operational feedback. *(Was P1; same scope as FR-023.)* |
| FR-047 | P0 | Link releases, deployments, quality results and incidents to commits and Change IDs. *(Was P1.)* |
| FR-100 | P0 | Correlate approved changes, releases, deployment versions, incidents and available runtime telemetry. *(Was P1.)* |
| FR-082 | P1 | Build cross-tool traceability across requirements, work items, PRs, tests, releases and incidents where evidence exists, including work done outside UnityStack. |
| FR-083 | P1 | Label AI-inferred associations separately from verified ones. |
| FR-089 | P2 | Reconstruct historical cross-tool traceability with evidence and explicit confidence. |

### 13.9 Coexistence, sync and ingestion

| ID | Priority | Requirement |
|---|---|---|
| FR-072 | P0 | Allow complete traditional SDLC work outside UnityStack with no dependency on its availability. |
| FR-079 | P0 | Allow end-to-end SDLC work without using the UnityStack UI. |
| FR-073 | P0 | Detect externally created or updated GitHub PRs, commits and branches. |
| FR-074 | P0 | Synchronise PR review, merge and available CI/CD status into UnityStack's derived state. |
| FR-075 | P0 | Link external records to Change or story IDs using source-backed identifiers and evidence. |
| FR-084 | P0 | Preserve source-system authority, stable source IDs, URLs, timestamps and sync status. |
| FR-092 | P0 | Preserve source authority, identity, timestamps and provenance in imported records. |
| FR-051 | P0 | Assign canonical ownership and sync direction to each artifact type and tool; detect divergence and block silent overwrites. |
| FR-076 | P1 | Keep unlinked external work and optionally suggest relationships without blocking it. *(Was P0.)* |
| FR-080 | P1 | Passively ingest external SDLC events and periodically reconcile source records. *(Was P0.)* |
| FR-091 | P1 | Provide an ongoing sync and reconciliation foundation for connected providers. *(Was P0.)* |
| FR-055 | P1 | Define durable events, idempotent handlers, retry and dead-letter behaviour and recovery for long-running workflows and webhooks. |
| FR-081 | P2 | Import available historical records from connected tools. *(Was P1.)* |
| FR-086 | P2 | Provide scoped initial import of accessible GitHub and tracker records. *(Was P1.)* |
| FR-087 | P2 | Support large-scale resumable incremental historical ingestion and backfill. |
| FR-088 | P2 | Connect existing enterprise providers (for example Jira, Jenkins, Confluence) through supported adapters. |
| FR-090 | P2 | Produce ingestion coverage, exceptions, provenance and reconciliation reports. |
| FR-085 | P2 | Support existing-organisation adoption alongside greenfield installations. *(Was P0; the adapter design in §8 keeps this possible.)* |

### 13.10 Workflow orchestration

| ID | Priority | Requirement |
|---|---|---|
| FR-095 | P1 | Generate actionable work items from approved workflow events; support parallel work and blocking dependencies. |
| FR-096 | P1 | Provide My Work assignment, pickup, review and exception queues. |
| FR-101 | P1 | Allow workflow recovery and replay without duplicate downstream actions or unauthorised transitions. |

### 13.11 Projects, repositories and Inception

| ID | Priority | Requirement |
|---|---|---|
| FR-102 | P0 | Any signed-in user can start a new project from a single entry point that opens an AI-led inception conversation; no creation form is provided. **Built.** |
| FR-103 | P0 | The AI captures mandatory items (name, problem, target users, product type, expected benefit, scale) through adaptive questioning, and records unknowns as open questions. **Built.** |
| FR-104 | P0 | Show a live read-only Project Charter preview that updates as answers are captured, with a readiness indicator. **Built.** |
| FR-105 | P0 | Create the project only after the user explicitly confirms a summarised charter; abandoned inception conversations are discarded. **Built.** |
| FR-106 | P0 | Persist the project, charter revisions, open questions and inception conversation with actor and agent-run references. **Built.** |
| FR-107 | P0 | Allow charter changes via AI conversation and name/key/status changes via a simple settings panel, each producing an audited revision. |
| FR-108 | P1 | When a GitHub repository is linked or created, propose the charter to Git via PR; after merge, treat Git as authoritative. |
| FR-011 | P0 | Support multiple projects within one UnityStack installation. |
| FR-012 | P0 | Register one or more GitHub repositories per project. |
| FR-014 | P0 | Map artifact types to repository, branch and path; agents resolve destinations through the registry, not hard-coded folders. |
| FR-013 | P1 | Support monorepo, multi-repo, repository-per-service and hybrid arrangements. *(Was P0.)* |
| FR-015 | P1 | Allow multiple repositories for the same artifact type. *(Was P0.)* |
| FR-016 | P2 | Support shared organisational repositories for standards, libraries and templates. *(Was P1.)* |

### 13.12 Identity and security

| ID | Priority | Requirement |
|---|---|---|
| FR-052 | P0 | All agent action requests pass through a trusted policy, authorisation and execution layer with scoped credentials and approval checks. |
| FR-049 | P1 | Provide central identity, session management and platform roles; map platform identities to tool access where supported and document exceptions. *(Was P0. MVP minimum: any deployed environment requires authentication — see §14.)* |

### 13.13 Design and documentation tools

| ID | Priority | Requirement |
|---|---|---|
| FR-038 | P1 | Generate Mermaid diagrams and ADRs through human-reviewed PRs. |
| FR-039 | P2 | Publish Markdown documentation through MkDocs and OpenAPI specifications through Swagger UI. *(Was P1.)* |
| FR-037 | P2 | Generate and review Excalidraw wireframes and Penpot designs with Git-backed artifacts. *(Was P1.)* |
| FR-040 | P2 | Register software components in Backstage using Git-versioned catalogue descriptors. *(Was P1.)* |

### 13.14 Distribution and platform operations (Later)

| ID | Priority | Requirement |
|---|---|---|
| FR-001 | P2 | Deliver a versioned UnityStack distribution with infrastructure modules, deployment definitions, configuration templates and installer scripts. *(Was P0.)* |
| FR-002 | P2 | Accept organisation-specific cloud region, domain, GitHub organisation, AI provider and deployment profile settings. *(Was P0.)* |
| FR-003 | P1 | Use Terraform to provision required AWS resources with repeatable plan and apply workflows. *(Was P0; timing depends on D-2.)* |
| FR-004 | P2 | A full installation profile deploys UnityStack and all nine default tools with their dependencies. *(Was P0.)* |
| FR-035 | P2 | Deploy and expose all nine default tools. *(Was P0.)* |
| FR-006 | P2 | Make installation idempotent where practical; rerunning must not destroy persistent data. *(Was P0.)* |
| FR-007 | P2 | Pin application and infrastructure versions in a release manifest. *(Was P0.)* |
| FR-008 | P1 | Provide backup and restore of UnityStack data, health checks and documented upgrade steps. *(Was P0.)* |
| FR-009 | P2 | Support optional core and custom installation profiles. |
| FR-010 | P2 | Provide scheduled drift checks, update recommendations and operational dashboards. |
| FR-048 | P2 | Separate platform installation/upgrade/recovery from the lifecycle of customer software, with separate versions, roles and audit records. *(Was P0.)* |
| FR-054 | P2 | Record installation phase outcomes, readiness failures, backup verification and repeat-deployment results in an inspectable report. *(Was P0.)* |
| FR-057 | P2 | Provide onboarding templates and validation for domain, region, repositories, identity, AI settings and tool endpoints. *(Was P1.)* |
| FR-059 | P2 | Provide cost and resource reporting and sizing profiles based on measured usage. *(Was P1.)* |

## 14. Security, identity and governance

1. AI output is attributable to an agent run (provider and model) and a source request.
2. Agents may draft but may not merge protected PRs, obtain unrestricted cloud credentials or deploy to production without the applicable policy and approval checks.
3. **Trusted action boundary:** the LLM proposes an action; non-LLM code checks policy, identity, target, schema, budget and approval before executing it.
4. **Identity model:** distinguish the human requester or approver, the logical AI agent, and the trusted execution service; record all three.
5. **Least privilege:** GitHub App with scoped permissions, short-lived credentials where available, separate read / propose / execute privileges.
6. **Untrusted input:** repository files, retrieved documents, tool output, issue text and **logs** are untrusted; instructions inside them never override system policy.
7. **Secrets** never go into Git or model prompts unnecessarily. Real keys live only in `.env` (git-ignored) locally, or a secrets manager when deployed; `.env.example` holds placeholders only.
8. **MVP minimum for a sole user:** local use may run without sign-in; any environment reachable from the internet must require authentication and HTTPS.
9. Sandboxed code execution, secret redaction, outbound access controls and emergency revocation are required as the developer agent matures.
10. Every adapter defines its authority, sync direction, conflict handling and audit events.

**Acceptance:** an AI agent cannot merge a protected PR, obtain an unrestricted cloud credential or deploy to production without the applicable policy and approval checks.

## 15. Architecture and deployment

### 15.1 Current implementation (as of 27 November 2026)

| Part | Implementation |
|---|---|
| Frontend | Vue 3 + Vite, served by nginx (`frontend/`) |
| Control plane | Java + Spring Boot (`backend/`), Flyway migrations |
| AI | Inside the Spring Boot backend, behind the provider-neutral `InceptionAgent` interface; mock and OpenAI implementations. **No separate Python service yet** (see D-1). |
| State | PostgreSQL |
| Runtime | Local Docker Compose (PostgreSQL + backend + frontend) |
| Cloud | None yet (see D-2) |

### 15.2 Target logical architecture

```mermaid
flowchart TB
  User[User] --> Portal[Vue workspace]
  Portal --> API[Spring Boot control plane]
  API --> Registry[(Project / repository / capability registry)]
  API --> Agents[AI agents]
  Agents --> LLM[LLM adapter]
  API --> Policy[Trusted policy and execution]
  Policy --> Adapters[Capability adapters]
  Adapters --> GitHub[GitHub]
  Adapters --> PM[Project management: OpenProject / Jira]
  Adapters --> CI[CI/CD: GitHub Actions]
  Adapters --> Obs[Observability]
  CI --> Env[Deployed environment]
  Env --> Obs
  Obs --> Agents
```

- **Control plane:** portal, API, registry, identity and policy, workflow state.
- **AI plane:** agents and the LLM adapter; models hold no direct infrastructure permissions.
- **Integration plane:** capability adapters, optionally exposed via MCP behind trusted execution.
- **Data plane:** GitHub for canonical artifacts; PostgreSQL for application and workflow data; tool databases for tool data; the observability provider for logs and metrics.
- **Deployment plane:** CI/CD deploys the managed application; infrastructure as code provisions environments.

### 15.3 Artifact types and repository flexibility

UnityStack must not assume one repository or fixed folders.

| Artifact type | Typical Git representation |
|---|---|
| Requirements, epics, stories, bugs | Markdown / YAML |
| Wireframes | `.excalidraw` JSON |
| UX artifacts | Design exports, tokens, prototype source |
| Architecture | Mermaid, ADR Markdown |
| API contracts | OpenAPI YAML/JSON |
| Application code and tests | Source files |
| Infrastructure | Terraform / deployment manifests |
| CI/CD | GitHub Actions YAML |
| Observability configuration | Dashboard, alert and SLO definitions |
| Documentation | Markdown, `mkdocs.yml` |
| Catalogue metadata | `catalog-info.yaml` |

Illustrative repository registry (not a final schema):

```yaml
organization: abc-bank
project: digital-banking
repositories:
  - id: requirements
    provider: github
    repository: abc-bank/banking-requirements
    defaultBranch: main
    artifacts:
      - type: requirement
        path: requirements/
      - type: story
        path: stories/
  - id: customer-service
    provider: github
    repository: abc-bank/banking-customer-service
    defaultBranch: main
    artifacts:
      - type: source-code
        path: src/
      - type: unit-test
        path: src/test/
```

### 15.4 Later: enterprise distribution and onboarding (P2)

- **Distribution:** versioned package; Terraform-provisioned AWS (EC2 + Docker Compose first, EKS later); S3 remote state with locking; IAM least privilege and GitHub OIDC; Secrets Manager or SSM; ECR; TLS; pinned versions in a release manifest; installer, upgrade, validate and uninstall scripts; full nine-tool profile with health checks.
- **Platform lifecycle:** install, configure, upgrade, rollback/recovery and uninstall are separate from the lifecycle of managed software; a platform upgrade never triggers a product release. Irreversible migrations are never promised automatic rollback.
- **Adoption modes:** greenfield (deploy with default tools) or existing organisation (connect existing tools, ingest accessible history, keep teams' own processes).
- **Onboarding steps:** discover and configure providers and permissions; connect with least privilege, read-only first; start event capture before or with backfill; import history in resumable batches; normalise records while keeping source IDs and provenance; build verified links, then optionally propose inferred ones; report completeness and gaps; enable steady-state sync and health alerts.
- **Tenancy:** one independent installation per organisation; multi-tenant SaaS only if later required.

## 16. Non-functional requirements

| ID | Category | Requirement |
|---|---|---|
| NFR-001 | Reproducibility | The same version and configuration produce functionally equivalent deployments. |
| NFR-002 | Security | Encrypt data in transit and at rest where supported; least privilege; protected secrets; audited administrative access. |
| NFR-003 | Isolation | Each organisation has its own installation, credentials, databases and resources. |
| NFR-004 | Extensibility | Integrations use stable capability contracts, never provider-specific logic in agents or workflows. |
| NFR-005 | Maintainability | Tool images and versions are pinned, documented and independently upgradeable where possible. |
| NFR-006 | Reliability | Failures are detected and reported; persistent data survives routine redeployments. |
| NFR-007 | Observability | Record platform health, errors, deployment outcomes and AI run metadata. |
| NFR-008 | Cost | Nonessential services can be switched off; cloud and LLM cost drivers are documented before larger deployments. |
| NFR-009 | Portability | Cloud-specific logic stays in infrastructure modules; provider-specific logic stays in adapters. |
| NFR-010 | Performance | Response-time and capacity targets are set after a baseline measurement. |
| NFR-011 | Recovery | Backup and restore of persistent data are documented and tested. |
| NFR-012 | Accessibility | Portal navigation and controls are accessible. |
| NFR-013 | Model independence | **New.** Switching LLM provider or model needs configuration only and loses no project context. |
| NFR-014 | Local-first development | **New.** The whole MVP loop can run locally with Docker and the mock LLM, without cloud resources. |

Quantitative targets (install time, cloud spend, SLOs, RTO, RPO) are deliberately TBD until measured.

## 17. MVP acceptance — the thin loop

The MVP is accepted when the sole user can do the following on a demo application (Hello World first), with every step linked by one Change ID:

1. Create a project through Inception and link its GitHub repository; the charter is proposed to Git via PR.
2. Discuss a requirement change with AI, inspect the diff, finalise a revision and get a PR in the mapped repository.
3. After merge, have AI propose epics and stories; after their PR merges, they are published once to the tracker, even if events are replayed.
4. Pick a story; AI proposes code and tests together on a branch; the PR carries the Change ID.
5. Merge the PR under repository policy; CI builds, tests and deploys to the environment.
6. See the deployed version's logs, tagged with the Change ID, from UnityStack.
7. Ask AI whether things work as expected; AI answers with cited log evidence.
8. Accept an AI-proposed bug; it appears in the tracker linked to the Change ID, deployment and log evidence, and can start the next change.
9. View one timeline for the Change ID from requirement to bug; missing links show as gaps.
10. Switch the LLM provider or model by configuration mid-project and continue working without losing context.
11. A PR created directly in GitHub, outside UnityStack, is detected and shown with its source status.
12. No AI action merges, deploys to production or creates a tracker item without the required human action.

## 18. Open decisions

| ID | Decision | Current state |
|---|---|---|
| D-1 | Separate Python AI service (LangGraph) or keep AI in Spring Boot | AI currently runs in Spring Boot behind an interface. Python is deferred; decide when agent workflows need it. |
| D-2 | When a cloud (AWS) environment becomes required | Loop can run locally; a deployed environment is needed for the delivery and observe stages. Decide whether the first one is local Docker or AWS. |
| D-3 | Observability provider for logs and metrics | Not among the nine tools. Open-source candidates to evaluate, for example Grafana with Loki and Prometheus, OpenSearch, or SigNoz. |
| D-4 | Change ID format and how it is carried | For example `<PROJECTKEY>-CHG-001`, in branch names, commit trailers, PR labels, build metadata and log fields. |
| D-5 | First project-management provider in the loop | OpenProject is the default; Jira follows as the second adapter. |
| D-6 | CI/CD provider | GitHub Actions as the default behind the CI adapter. |
| D-7 | Review policy for a sole user | GitHub does not let an author approve their own PR. Decide on self-merge rules for the sole-user phase. |
| D-8 | OpenProject sync direction | Git-to-tool for approved definitions with field-level ownership (§11); controlled tool-to-PR proposals later. |
| D-9 | Penpot export fidelity and authoritative design format | Later (P2). |
| D-10 | Tool versions, licensing and distribution rights | Verify before packaging (P2). |
| D-11 | Adapter, event envelope and schema contracts | Define in HLD; the event envelope includes `eventId`, `eventType`, `occurredAt`, `organisationId`, `projectId`, `correlationId`, `changeId`, `actor`, `artifactRefs`, `schemaVersion`. |
| D-12 | Identity provider and SSO | Later; evaluate Keycloak with OIDC/SAML per tool. |
| D-13 | Scale, SLOs, RTO and RPO | After baseline measurement. |

## 19. Risks

| Risk | Mitigation |
|---|---|
| Building every stage deeply before the loop works end to end | Thin loop first (§17), then deepen stage by stage |
| Adapter interfaces shaped around one provider | Design interfaces from capabilities; add a second provider (Jira) early |
| AI corrupts approved documents | Baseline SHA, schema checks, read-only preview, explicit finalisation, PRs |
| Agents get excessive permissions | Trusted execution layer, least privilege, sandboxing, human approval |
| Prompt injection through logs or repository content | Treat all retrieved content as untrusted data; no execution authority from it |
| GitHub and UnityStack drift | Webhooks plus periodic reconciliation; GitHub stays authoritative |
| Secrets committed by mistake | Placeholders only in `.env.example`; GitHub push protection; secret scanning |
| LLM and cloud costs grow | Mock provider for development; spending limits; small footprint |
| Faulty changes deployed | Tests, protected branches, health checks, rollback, feature flags |
| Tool licensing or SSO limits | Verify per tool before packaging |
| Solo capacity | Keep scope to the thin loop; defer P2 entirely |

## Appendix A — Change history and section map

| Version | Date | Summary |
|---|---|---|
| 1.0–1.1 | Nov 2026 | Initial PRD; platform lifecycle, identity, ownership, registry, events, traceability additions (§11A–H, FR-048 to FR-060). |
| 1.2 | 21 Nov 2026 | AI-first two-pane UX, coexistence, Change ID, enterprise adoption (§17–21, FR-061 to FR-101). |
| 1.3 | Nov 2026 | Project Inception (§22, FR-102 to FR-108). |
| 2.0 | 27 Nov 2026 | Consolidated into one current-state document; product decisions PD-1 to PD-3; priorities reset; adapter architecture and closed loop made central; new FR-109 to FR-116 and NFR-013/014; observability added as a capability. |

| Old section | New section |
|---|---|
| §1 Executive summary, §2 Objectives | §2, §4 |
| §3 Scope, §21 Updated MVP | §6, §17 |
| §4 Stakeholders | §5 |
| §5 Tool stack | §8.3 |
| §6.1–6.6, §6.7 Functional requirements | §13 |
| §7 Artifact types | §15.3 |
| §8 Reference architecture | §15.2 |
| §9 AWS deployment | §15.4 |
| §10 Governance, §11B Identity | §14 |
| §11 NFRs | §16 |
| §11A Platform lifecycle | §15.4 |
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
| §20 Enterprise adoption | §15.4 |
| §22 Project Inception | §10 |

## Appendix B — Architecture decision records to prepare

| ADR | Subject |
|---|---|
| ADR-001 | Independent single-tenant installation per organisation |
| ADR-002 | GitHub canonical artifact ownership and review governance |
| ADR-003 | Cloud provisioning boundary (AWS / Terraform) |
| ADR-004 | Docker Compose runtime and evolution path |
| ADR-005 | AI service placement: Spring Boot or separate Python service (D-1) |
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
| ADR-016 | **New.** Observability provider and log-to-Change-ID correlation (D-3, D-4) |

## Appendix C — Next documents

- System architecture and C4 diagrams
- Capability adapter contracts (including LLM and observability)
- Change ID and traceability schema
- Git repository and artifact schema
- MVP user stories and acceptance tests for the thin loop
- Security threat model and runbooks
- Installation and upgrade specification (Later)
