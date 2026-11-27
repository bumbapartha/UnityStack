# TD-08 — Feature flags: OpenFeature with Unleash

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Features need switching on and off per environment without redeploying, without lock-in to one flag tool.

## Decision

The code calls the OpenFeature SDK; Unleash is the flag server. An organisation can write its own OpenFeature provider and plug it in.

## Consequences

OpenFeature acts as a ready-made adapter. P1.

## Related

[Delivery and testing](../requirements/delivery-and-testing.md), [Tools and adapters](../tools-and-adapters.md)
