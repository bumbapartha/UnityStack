# PD-5 — Open-source defaults, swappable by adapter

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Organisations adopting UnityStack should pay only for the infrastructure it runs on (AWS, Azure or on-premises), not for tool licences.

## Decision

Every default provider is open source and sits behind an adapter. Changing a provider means adding or switching an adapter and its configuration only. Later, an organisation picks the provider for each capability at install time.

## Consequences

Non-functional requirement [NFR-015](../requirements/non-functional.md) (replaceability). Install-time tool choice is part of distribution (P2).

## Related

[Tools and adapters](../tools-and-adapters.md), [Distribution and operations](../requirements/distribution.md), [Non-functional requirements](../requirements/non-functional.md)
