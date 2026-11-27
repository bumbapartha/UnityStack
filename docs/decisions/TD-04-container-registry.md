# TD-04 — Container registry: GitHub Container Registry, then ECR

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Images need a registry once CI deploys to cloud environments.

## Decision

GitHub Container Registry first, an Amazon ECR adapter with the AWS move, and Harbor later. Organisations choose at install time.

## Consequences

Not needed while developing locally (images are built locally); arrives with the AWS move.

## Related

[Delivery and testing](../requirements/delivery-and-testing.md), [Tools and adapters](../tools-and-adapters.md)
