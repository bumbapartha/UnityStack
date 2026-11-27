# AI workspace

[← Documentation index](../README.md)

The two-pane AI workspace shared by every module.

**Tools:** [AI models](../tools-and-adapters.md#ai-models)

## Workspace model

- **Two panes:** left, the AI conversation (questions, clarifications, progress, references); right, a continuously refreshed read-only preview. Narrow screens may stack the panes without losing either.
- **Role-specific previews:** requirements → BRD/PRD Markdown; planning → epic/story hierarchy; architecture → HLD, ADRs, rendered Mermaid and source diff; development → changed files and code diff with build/test feedback; testing → test cases and results; delivery → build, release and deployment state; observe → log excerpts and AI findings.
- **Revisions:** every AI proposal creates a draft revision with an immutable ID, timestamp, actor, agent-run reference, source baseline (Git commit SHA where applicable) and parent revision. The preview offers current, diff and history views. An accepted draft is never overwritten silently.
- **Draft lifecycle:** `OPEN → PROPOSED → USER_ACCEPTED → PR_OPEN → MERGED`, or `REVISE / REJECT / CLOSED`. PR and merge states are read from GitHub, not inferred from chat.
- **Finalisation:** the user explicitly accepts a specific revision; trusted code validates schema, permissions and destination before creating the branch, commit and PR. Finalising does not merge.
- **Conflicts:** if the upstream baseline changed since the draft started, show the conflict and offer rebase or restart; never overwrite.
- **Manual editing:** none inside UnityStack in the MVP. External editors, IDEs and GitHub remain available. An "open in editor" action is P2.

## Workspace and artifact contract

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

## Requirements

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
