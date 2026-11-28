# Communication

[← Requirements](README.md)

Cross-cutting: notifications, team chat and meetings.

**Tools:** [Notifications](../tools-and-adapters.md#notifications), [Meetings](../tools-and-adapters.md#meetings)

**Decisions:** [TD-09](../decisions.md#td-09), [TD-11](../decisions.md#td-11)

## Notifications

| ID     | Priority | Requirement                                                                                                                                |
| ------ | -------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| FR-124 | P1       | **New.** Send notifications (review requests, PR status, alerts, incident summaries, publication failures) through a notification adapter. |

## AI meeting assistant (future)

| ID     | Priority | Requirement                                                                                                                                                                                                                                                                                                                                            |
| ------ | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| FR-126 | P2       | **New.** An AI participant can join requirement meetings (with the participants' consent) through a meeting adapter; it listens, raises questions, suggests solutions when asked, produces a summary and transcript on request, and afterwards proposes requirement updates in the requirements workspace, which follow the normal finalise → PR flow. |
