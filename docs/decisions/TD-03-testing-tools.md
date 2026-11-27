# TD-03 — Testing: JUnit, Vitest, Playwright, Kiwi TCMS

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Tests must be traceable from requirement to release, with history kept per release.

## Decision

JUnit and Vitest for unit tests, Playwright for end-to-end tests, and Kiwi TCMS for test cases, plans, runs and release reports. Test cases are authored in Git; Kiwi keeps run history.

## Consequences

Every release has a permanent record of what was tested and what passed, linked to requirements and Change ID.

## Related

[Delivery and testing](../requirements/delivery-and-testing.md), [Development and Git](../requirements/development-and-git.md), [Tools and adapters](../tools-and-adapters.md)
