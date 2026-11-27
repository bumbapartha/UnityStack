# Coexistence and sync

[← Documentation index](../README.md)

Working alongside teams that use their own tools, without blocking them.

**Tools:** [Source control](../tools-and-adapters.md#source-control), [Project management](../tools-and-adapters.md#project-management)

**Decisions:** [AD-4](../decisions/AD-4-git-provider-adapter.md)

## Source authority, sync and coexistence

- **UnityStack is never a mandatory gateway.** A team can create stories in the tracker, code in a local IDE, open and merge PRs in GitHub, run CI/CD and deploy without opening UnityStack.
- **Authority:** the connected Git provider (GitHub first) owns repository, branch, commit, PR, review and merge truth. The tracker owns ticket and status records. CI/CD owns build and deployment execution. The monitoring system owns logs and metrics. UnityStack owns orchestration state, AI drafts, indexes, correlations and inferred links.
- **Artifact mapping record:** stable artifact ID, type, owning repository/path/branch, canonical format, originating tool, sync direction, last synced commit, tool revision and sync status.
- **Canonical lifecycle:** Draft → Validate → Proposed PR → Review → Merge → Synchronise connected tools → Verify.
- **Field-level ownership:** native tool edits are allowed. Operational fields (for example tracker status) belong to the tool; Git-approved fields (for example acceptance criteria) belong to Git. Drift is reported, never silently overwritten; conflicts show both versions and a resolution path.
- **Ingestion:** webhooks where available, API polling, checkpoints and scheduled reconciliation. Duplicate and out-of-order events are handled idempotently. Ingesting never modifies the source record.
- **Unlinked work:** PRs or stories without a matching Change ID are kept and shown; UnityStack may suggest links but never fabricates them or blocks the work.
- **Idempotency:** idempotency keys and optimistic revision checks prevent duplicate PRs, duplicate tracker items and replayed updates. Failed publication is visible, retryable and audited, and an unmerged proposal is never treated as approved.
- **Limits:** UnityStack cannot reconstruct records deleted before ingestion, outside granted access, not exposed by APIs or expired under the source's retention. It promises best-effort capture of accessible data and transparent gap reporting, not zero data loss.

## Requirements

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
| FR-085 | P2 | Support existing-organisation adoption alongside greenfield installations. *(Was P0; the adapter design in [Adapters and capability registry](adapters.md) keeps this possible.)* |
