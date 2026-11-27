# TD-10 — Local rebuilds, no CI deployment while local

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-16

## Context

During local development, CI cannot usefully deploy to a laptop, and rebuilding after every AI file edit would restart containers constantly.

## Decision

No CI deployment locally. After a batch of edits, the AI runs `docker compose up --build -d <service>`; for manual edits, `docker compose watch`. GitHub Actions still runs tests and scans on every PR. After the AWS move, each PR gets a preview environment and merges to main deploy to the main (production) environment.

## Consequences

The container registry moves out of the MVP to the AWS move.

## Related

[Development setup](../dev-setup.md), [Delivery and testing](../requirements/delivery-and-testing.md), [Scope and roadmap](../roadmap.md)
