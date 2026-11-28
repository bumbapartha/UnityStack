# Core platform

[← Requirements](README.md)

Cross-cutting: the parts of UnityStack itself that every SDLC phase relies on — adapters, AI and LLM, and the shared AI workspace.

**Tools:** [AI models](../tools-and-adapters.md#ai-models), [Source control](../tools-and-adapters.md#source-control), [Project management](../tools-and-adapters.md#project-management)

**Decisions:** [PD-2](../decisions.md#pd-2), [AD-1](../decisions.md#ad-1)

## Adapters and capability registry

- Workflows and agents call **logical operations** on a capability, for example `projectManagement.createStory`, `scm.openPullRequest`, `ci.getRunStatus`, `observability.queryLogs`, `llm.complete`.
- An **adapter** translates those operations to one provider's API. Provisioning a tool and connecting to an existing instance are separate adapter concerns.
- A **capability registry** records, for each capability in each installation: provider ID, connection mode (provisioned or existing), endpoint, credential reference, supported operations, health and scope.
- Unsupported or edition-restricted operations return an explicit "unsupported operation" error; callers must handle it.
- MVP needs one working provider per capability used in the loop. The interfaces must not leak provider-specific concepts, so that a second provider can be added without changing workflows.
- **Swap rule:** changing the provider of a capability means adding or selecting an adapter and changing configuration — nothing else in UnityStack changes ([NFR-015](12-non-functional.md)).
- **Selection:** today the provider is chosen in configuration. Later, an organisation chooses the provider for each capability at install time, and the installer deploys or connects only those (FR-043, FR-060).
- Where a provider already has a standard client abstraction, the adapter may use it (for example OpenFeature for feature flags, OpenID Connect for identity). Where an MCP server exists for a tool, an adapter may call it, subject to the trusted execution rules ([Security](07-security.md)); community MCP servers are checked for maturity before use.

### Adapter requirements

| ID     | Priority | Requirement                                                                                                                                                                                                                                                                                   |
| ------ | -------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FR-109 | P0       | **New.** Every external tool is accessed only through a capability adapter; workflows and agents contain no provider-specific API calls.                                                                                                                                                      |
| FR-042 | P0       | Provide a capability registry and stable provider-independent interfaces for every capability used in the MVP loop.                                                                                                                                                                           |
| FR-050 | P0       | The registry maps logical operations to installed or connected provider adapters, with endpoint, credential reference, supported operations and health.                                                                                                                                       |
| FR-005 | P0       | Show each configured tool in the UnityStack portal with endpoint, connection mode and health. _(Was: show all nine installed tools.)_                                                                                                                                                         |
| FR-127 | P2       | **New.** Support further Git providers behind the source-control adapter (Gitea, GitLab, Bitbucket, Azure DevOps Repos), mapping branches, commits, pull/merge requests, reviews, branch protection and webhooks; operations a provider lacks return an explicit unsupported-operation error. |
| FR-112 | P1       | **New.** Add Jira as a second project-management provider behind the same adapter, proving that the provider can be swapped without workflow changes.                                                                                                                                         |
| FR-043 | P2       | Allow each capability to use a UnityStack-provisioned default, an existing customer instance, or a supported alternative provider, chosen by the customer.                                                                                                                                    |
| FR-060 | P2       | Allow users to select an alternative provider, connect an existing enterprise instance, or provision a supported alternative per capability (BYOT) through the UI.                                                                                                                            |

## AI and LLM

- The LLM is reached only through a provider-neutral interface (today: `InceptionAgent` with mock and OpenAI implementations). Provider and model are chosen by configuration.
- A **mock provider** is always available for offline development and automated tests.
- All context an agent needs — conversation, charter, drafts, approved artifacts, decisions, trace links — is stored in UnityStack, Git or the connected tools. Nothing depends on provider-side memory, threads or assistants.
- Switching provider or model takes effect for the next agent turn. The new model is given context rebuilt from those stores, so in-progress work continues.
- Every agent run records the provider and model used (`agentRef`, for example `openai:gpt-4.1-mini`) for audit.
- **Portable AI module ([AD-1](../decisions.md#ad-1)):** agents, prompts and LLM adapters live in a separate module of the Java code base. The module exposes interfaces with plain data objects (no framework or persistence types), contains no Spring-specific code, and is called by the rest of the application only through those interfaces. This keeps a later move to a separate (for example Python) AI service a contained change ([NFR-016](12-non-functional.md)).

### AI requirements

| ID     | Priority | Requirement                                                                                                                                                                                                                                          |
| ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FR-026 | P0       | Provide an LLM adapter that switches model provider and model by configuration, with no workflow changes.                                                                                                                                            |
| FR-110 | P0       | **New.** Always provide a mock LLM provider for offline development and automated tests.                                                                                                                                                             |
| FR-111 | P0       | **New.** Keep all agent context in UnityStack, Git or connected tools; after a provider or model switch, the next agent turn receives context rebuilt from those stores, with no dependency on provider-side memory.                                 |
| FR-027 | P0       | Provide an AI business-analyst workflow that turns conversation into structured requirements-as-code.                                                                                                                                                |
| FR-028 | P0       | Separate AI reasoning and requested actions from trusted identity, authorisation, validation, approval and tool execution.                                                                                                                           |
| FR-029 | P0       | Enforce configured reviewer approval for merges (review may be optional where permitted), mandatory policy and quality gates, and separately configured production deployment authorisation. User finalisation of an AI proposal is always explicit. |
| FR-030 | P0       | Record agent execution state, provider and model, failures, retries and tool-call audit events.                                                                                                                                                      |
| FR-031 | P0       | Add role agents progressively: requirements, planning, developer (code and tests), and observe/analyse agents are needed for the MVP loop; UX, architecture and operations agents follow. _(Was P1.)_                                                |
| FR-032 | P1       | Introduce MCP-based tool integration where it adds value, with controlled permissions and validation.                                                                                                                                                |
| FR-033 | P1       | Add RAG over authorised project documents, agent evaluation, and model cost and latency tracing.                                                                                                                                                     |
| FR-034 | P1       | Enforce retry limits, budgets, timeouts and human escalation for failed or ambiguous agent actions.                                                                                                                                                  |

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

| Module       | Right-hand preview                                        | First relevant capability                           |
| ------------ | --------------------------------------------------------- | --------------------------------------------------- |
| Requirements | BRD/PRD Markdown, inline changes                          | AI edits an existing Git Markdown document          |
| Architecture | HLD/LLD, ADR, rendered Mermaid and source diff            | Generate/revise UnityStack architecture             |
| Planning     | Epic/story/bug hierarchy, acceptance criteria and changes | Decompose approved requirements into proposed work  |
| Development  | Changed files, code diff, build/test feedback             | AI proposes code PRs with bounded execution         |
| Testing      | Test scenarios, test code, result summary                 | Generate tests and review coverage                  |
| Delivery     | Build/release/deployment state and links                  | Track release to environment and feature            |
| Intelligence | Cross-tool timeline and traceability graph                | Explain past work even when done outside UnityStack |

### Workspace requirements

| ID     | Priority | Requirement                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| ------ | -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FR-071 | P0       | Use AI conversation and a read-only live preview for all UnityStack-native artifact creation.                                                                                                                                                                                                                                                                                                                                                                        |
| FR-093 | P0       | Provide role-specific two-pane workspaces on a common preview and revision framework.                                                                                                                                                                                                                                                                                                                                                                                |
| FR-094 | P0       | Record revision history and show differences for each supported MVP artifact type.                                                                                                                                                                                                                                                                                                                                                                                   |
| FR-020 | P0       | Show the proposed artifact and diff before publication or approval.                                                                                                                                                                                                                                                                                                                                                                                                  |
| FR-064 | P0       | Apply conversational revisions to versioned previews and show inline or structured diffs.                                                                                                                                                                                                                                                                                                                                                                            |
| FR-065 | P0       | Require explicit user finalisation of a specific draft revision before creating a GitHub PR.                                                                                                                                                                                                                                                                                                                                                                         |
| FR-131 | P0       | **New.** Show design artifacts read-only in the workspace preview pane: Excalidraw wireframes rendered from Git, Penpot designs as exported images or an embedded prototype, and OpenAPI contracts through Swagger UI. An "Open in tool" action opens the external tool through its adapter for editing ([TD-13](../decisions.md#td-13)).                                                                                                                            |
| FR-137 | P0       | **New.** Offer a voice conversation in every AI chat: a microphone button next to Send starts a hands-free conversation with the AI. What the user says becomes their message in the same conversation, and replies are shown as text and read aloud. Typing works at any time. Only the text transcript is stored, not the audio. Speech goes through a speech adapter ([TD-16](../decisions.md#td-16)); if no speech provider is configured, the button is hidden. |
| FR-138 | P0       | **New.** Navigation: a top bar with UnityStack, the project switcher and the user. Before a project is open there is no phase navigation. Inside a project, a slim left rail lists Overview, Requirements, Design, Development, Testing, Deployment, Monitoring and Traceability, with Settings at the bottom; the current item is highlighted and every phase is one click away.                                                                                    |
| FR-077 | P2       | Offer launch-in-browser or local editor for manual artifact changes.                                                                                                                                                                                                                                                                                                                                                                                                 |
| FR-078 | P2       | Optionally support direct artifact editing inside UnityStack.                                                                                                                                                                                                                                                                                                                                                                                                        |
