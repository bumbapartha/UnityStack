# AD-2 — Local Docker until the thin loop works, then AWS

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-2

## Context

A cloud environment costs money and setup time; the loop can run locally.

## Decision

All tools run locally in Docker Compose, grouped by profiles. Once the thin loop works end to end, move to AWS as soon as possible (Terraform, EC2 + Docker Compose). Moving only the heavy tools to one EC2 host first is allowed.

## Consequences

Laptop memory is the main constraint, handled with Compose profiles. AWS is roadmap Phase 5.

## Related

[Development setup](../dev-setup.md), [Scope and roadmap](../roadmap.md)
