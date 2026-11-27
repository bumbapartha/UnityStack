# Requirements phase

[← Requirements](README.md)

Capturing what to build: project inception, requirements, epics and stories. The AI workspace used here is described in [Core platform](10-core-platform.md#ai-workspace).

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
