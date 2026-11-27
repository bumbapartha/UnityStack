# TD-07 — On-call: GoAlert

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Developers must be paged at night, with an AI investigation running alongside.

## Decision

GoAlert for on-call rotations, overrides, escalation policies and phone/SMS paging. Alerts also reach UnityStack, where AI adds an incident summary.

## Consequences

Rotations can be changed at any time, and its API lets UnityStack manage them later. Opsgenie, PagerDuty and OneUptime are possible later adapters.

## Related

[Observability and feedback](../requirements/observability-and-feedback.md), [Tools and adapters](../tools-and-adapters.md)
