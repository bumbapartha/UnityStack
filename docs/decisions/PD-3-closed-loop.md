# PD-3 — Product core: the closed loop joined by one Change ID

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

AI generates many artifacts quickly, which makes it hard to know what was built for what. AI drafting, traceability and the full lifecycle were the three candidate cores.

## Decision

The core is the closed loop: requirement → stories → code and tests on a branch → PR review → deployment → logs and monitoring → AI-assisted problem detection → accepted bug back in the tracker → next change, all joined by one Change ID.

## Consequences

AI drafting, traceability and the full lifecycle are built together, not in isolation. The Change ID exists from day one.

## Related

[Architecture](../architecture.md), [Traceability and orchestration](../requirements/traceability.md), [Observability and feedback](../requirements/observability-and-feedback.md)
