# AD-3 — Keycloak from the start

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

A simple built-in login now would need replacing later, with users, roles and sessions redone.

## Decision

Use Keycloak from the start for sign-in, users, groups and broad roles. UnityStack evaluates project-level, resource-level permissions. No temporary built-in login.

## Consequences

One more container in the core profile; realm configuration exported to Git without secrets.

## Related

[Identity and security](../requirements/identity-and-security.md), [Tools and adapters](../tools-and-adapters.md)
