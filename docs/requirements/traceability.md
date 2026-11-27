# Traceability and orchestration

[← Documentation index](../README.md)

The Change ID and the links that join every stage of the loop.

**Tools:** [Observability](../tools-and-adapters.md#observability)

**Decisions:** [PD-3](../decisions/PD-3-closed-loop.md), [TD-05](../decisions/TD-05-observability.md)

## Traceability and Change ID

- Every UnityStack-initiated change gets a stable **Change ID** at the start of the loop (format to be decided — see [D-4](../open-questions.md); for example `<PROJECTKEY>-CHG-001`).
- The Change ID is carried through: requirement and story IDs, branch name, commit trailer, PR title or label, CI run metadata, build and image version, deployment record, structured log fields, and bug records.
- External work keeps its own source IDs (tracker key, PR number, commit SHA) even when no Change ID exists yet.
- **Verified links** come from explicit references (an ID in a branch, commit, PR or ticket). **Inferred links** proposed by AI are labelled with confidence and evidence and stay unverified until a person confirms them.
- Runtime tracing IDs (for example OpenTelemetry `trace_id`) are distinct from the Change ID and are correlated through service version and deployment metadata.
- Goal: one end-to-end timeline per Change ID, reproducible from stored provenance, with missing links shown as gaps rather than filled in.

## Requirements

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

## Workflow orchestration requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-095 | P1 | Generate actionable work items from approved workflow events; support parallel work and blocking dependencies. |
| FR-096 | P1 | Provide My Work assignment, pickup, review and exception queues. |
| FR-101 | P1 | Allow workflow recovery and replay without duplicate downstream actions or unauthorised transitions. |
