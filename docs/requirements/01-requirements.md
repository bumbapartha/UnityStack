# Requirements phase

[← Requirements](README.md)

Capturing what to build: project inception, the AI workspace, requirements, epics and stories.

**Tools:** [AI models](../tools-and-adapters.md#ai-models), [Source control](../tools-and-adapters.md#source-control), [Project management](../tools-and-adapters.md#project-management)

## Project Inception (built)

### Specification

**Purpose:** capture a new project's very high-level intent through a short AI conversation instead of a form. Inception is deliberately simple; features, modules and detailed requirements are discovered later in the requirements and planning workspaces.

#### User journey

1. A signed-in user clicks **New Project**.
2. UnityStack opens the two-pane workspace: AI conversation on the left, live read-only **Project Charter** preview on the right.
3. The AI asks adaptive questions, one (or two closely related) at a time, skipping anything already answered.
4. When all mandatory items are captured, the workspace shows **Ready to create**; the AI presents a summary for confirmation.
5. The user explicitly confirms; UnityStack creates the project and offers next steps: connect or create a GitHub repository and start the requirements workspace.
6. If the user leaves before confirming, the inception conversation is discarded and nothing is created.

#### Information captured

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

#### Data and ownership

- On confirmation, UnityStack stores in PostgreSQL: the project record (ID, short key, name, status, product type, creator, timestamps), the charter as structured data plus rendered Markdown, open questions, and the inception conversation with agent-run reference.
- The charter is versioned: each change creates a new revision with parent revision, actor and agent-run reference.
- Until a GitHub repository is linked, PostgreSQL is authoritative for the charter. When a repository is linked or created, UnityStack proposes the current charter to the mapped Git path through a PR; after merge, Git is authoritative and later changes follow the normal draft → finalise → PR flow.
- After creation, charter content changes through AI conversation. Project name, key and status may also be changed in a simple project settings panel; those changes are audited.

**Acceptance:** a user creates a project by answering only the mandatory questions; the project and charter revision 1 exist in PostgreSQL; abandoning a second inception leaves no project; renaming the project creates an audit record.

### Inception, projects and repositories

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

## AI workspace

### Workspace model

- **Two panes:** left, the AI conversation (questions, clarifications, progress, references); right, a continuously refreshed read-only preview. Narrow screens may stack the panes without losing either.
- **Role-specific previews:** requirements → BRD/PRD Markdown; planning → epic/story hierarchy; architecture → HLD, ADRs, rendered Mermaid and source diff; development → changed files and code diff with build/test feedback; testing → test cases and results; delivery → build, release and deployment state; observe → log excerpts and AI findings.
- **Revisions:** every AI proposal creates a draft revision with an immutable ID, timestamp, actor, agent-run reference, source baseline (Git commit SHA where applicable) and parent revision. The preview offers current, diff and history views. An accepted draft is never overwritten silently.
- **Draft lifecycle:** `OPEN → PROPOSED → USER_ACCEPTED → PR_OPEN → MERGED`, or `REVISE / REJECT / CLOSED`. PR and merge states are read from GitHub, not inferred from chat.
- **Finalisation:** the user explicitly accepts a specific revision; trusted code validates schema, permissions and destination before creating the branch, commit and PR. Finalising does not merge.
- **Conflicts:** if the upstream baseline changed since the draft started, show the conflict and offer rebase or restart; never overwrite.
- **Manual editing:** none inside UnityStack in the MVP. External editors, IDEs and GitHub remain available. An "open in editor" action is P2.

### Workspace and artifact contract

**Common workspace:**

- Left pane: conversation, questions, clarifications, AI progress and references.
- Right pane: approved baseline, proposed revision, preview/diff/history tabs, validation feedback, finalise/create PR.
- Context: organisation, project, role/module, current work item, Git repo/path/ref, source artifact version and related change ID.
- Draft lifecycle: `OPEN → PROPOSED → USER_ACCEPTED → PR_OPEN → MERGED` (or `REVISE / REJECT / CLOSED`). PR and merge states are read from GitHub, not guessed from chat.
- Every proposed revision records source baseline, author/agent run, timestamp, diff and validation results. Drafts live in UnityStack storage; approved Git artifacts live in GitHub.
- MVP preview is **read-only**. Future: button to open artifacts in a supported web or local editor; no need to build that in the MVP.

| Module | Right-hand preview | First relevant capability |
|---|---|---|
| Requirements | BRD/PRD Markdown, inline changes | AI edits an existing Git Markdown document |
| Architecture | HLD/LLD, ADR, rendered Mermaid and source diff | Generate/revise UnityStack architecture |
| Planning | Epic/story/bug hierarchy, acceptance criteria and changes | Decompose approved requirements into proposed work |
| Development | Changed files, code diff, build/test feedback | AI proposes code PRs with bounded execution |
| Testing | Test scenarios, test code, result summary | Generate tests and review coverage |
| Delivery | Build/release/deployment state and links | Track release to environment and feature |
| Intelligence | Cross-tool timeline and traceability graph | Explain past work even when done outside UnityStack |

### Workspace requirements

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

## Requirements and planning

| ID | Priority | Requirement |
|---|---|---|
| FR-061 | P0 | Support conversational discovery and clarification of business requirements. |
| FR-062 | P0 | AI recommends story count, epic grouping, acceptance criteria and dependencies based on scope. |
| FR-063 | P0 | Show hierarchical epic/story and partial or full BRD previews. |
| FR-068 | P0 | After merge, publish approved epics and stories to the project-management tool through its adapter. *(Was: to OpenProject.)* |
| FR-069 | P0 | Make publication retryable and idempotent; no duplicate stories after retries or outages. |
| FR-070 | P0 | Show proposal → PR → merge → tool publication status and failure reasons. |
| FR-036 | P0 | Integrate the project-management adapter enough to link requirements and work items with Git-backed artifacts; document sync ownership and conflict handling. |
