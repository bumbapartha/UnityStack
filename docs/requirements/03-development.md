# Development phase

[← Requirements](README.md)

AI-proposed code and tests, branches and pull requests.

**Tools:** [Source control](../tools-and-adapters.md#source-control), [Testing](../tools-and-adapters.md#testing)

**Decisions:** [AD-4](../decisions.md#ad-4)

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-116 | P0 | **New.** For a story, the developer agent proposes code changes and matching tests together, on a branch, after reading the linked requirement, story, ADRs and repository conventions. |
| FR-018 | P0 | Treat approved GitHub content as the canonical version of durable, reviewable SDLC artifacts. |
| FR-019 | P0 | AI-generated changes are made on branches and proposed through PRs; no direct AI push to protected branches. |
| FR-021 | P0 | Validate generated artifacts against schemas and project rules before creating a PR. |
| FR-022 | P0 | Preserve actor, prompt/run reference, source requirement, artifact version, PR URL and approval status for audit. |
| FR-066 | P1 | Support configurable optional additional reviewer approval per organisation or repository. *(Sole user for now; see [D-7](../open-questions.md).)* |
| FR-067 | P1 | Permit automatic merge only where policy and branch protection allow and all required checks pass. *(Was P0.)* |
| FR-024 | P1 | Detect and reconcile changes made directly in connected tools to prevent uncontrolled divergence from Git. |
| FR-025 | P2 | Coordinate multi-repository changes without assuming atomic cross-repository merges. |
| FR-017 | P2 | Track related PRs across repositories as a logical change set with dependencies and merge status. *(Was P1.)* |
| FR-058 | P2 | Support dependency-aware multi-repository change sets with explicit handling of partial merges. *(Was P1.)* |
