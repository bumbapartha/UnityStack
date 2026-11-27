# TD-01 — CI/CD: GitHub Actions

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-6

## Context

CI/CD is needed for checks on every PR and, later, deployments.

## Decision

GitHub Actions is the default CI/CD provider behind the CI adapter. Jenkins and Argo CD are future adapters.

## Consequences

While developing locally, GitHub Actions runs checks only ([TD-10](TD-10-local-rebuild-workflow.md)).

## Related

[Delivery and testing](../requirements/delivery-and-testing.md), [Tools and adapters](../tools-and-adapters.md)
