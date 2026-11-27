# Observability and feedback

[← Documentation index](../README.md)

Logs, metrics, AI analysis, alerts, on-call and the bug feedback loop.

**Tools:** [Project management](../tools-and-adapters.md#project-management), [Observability](../tools-and-adapters.md#observability), [Alerting and on-call](../tools-and-adapters.md#alerting-and-on-call), [Notifications](../tools-and-adapters.md#notifications)

**Decisions:** [PD-3](../decisions/PD-3-closed-loop.md), [TD-05](../decisions/TD-05-observability.md), [TD-06](../decisions/TD-06-alert-rules-in-git.md), [TD-07](../decisions/TD-07-on-call-goalert.md), [TD-09](../decisions/TD-09-notifications.md)

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-113 | P0 | **New.** The deployed application emits logs, metrics and traces through OpenTelemetry, tagged with service name, service version and Change ID, with trace IDs in log lines; the OpenTelemetry Collector forwards them to the observability providers, and they are viewable from UnityStack. |
| FR-128 | P1 | **New.** Keep alert rules as files in Git, provisioned into the alerting tool; detect rules changed directly in the tool and propose them back to Git as a PR. |
| FR-114 | P0 | **New.** The user can ask AI to analyse logs for an environment and time window; the AI's findings cite the log entries they are based on. |
| FR-115 | P0 | **New.** AI may propose a bug from its findings; the bug is created in the tracker only after a person accepts it, and is linked to the Change ID, deployment and log evidence. |
| FR-046 | P0 | Connect production signals to incident or improvement proposals that re-enter the governed loop. *(Was P1.)* |
| FR-122 | P1 | **New.** Route alerts from alert rules to the on-call tool, which pages the on-call developer (phone, SMS, app) using rotations, overrides and escalation policies. |
| FR-123 | P1 | **New.** When an alert fires, notify UnityStack as well; AI investigates the related logs and metrics and attaches a summary with cited evidence to the incident while the developer is being paged. Any resulting bug still needs a person to accept it (FR-115). |
| FR-124 | P1 | **New.** Send notifications (review requests, PR status, alerts, incident summaries, publication failures) through a notification adapter. |
