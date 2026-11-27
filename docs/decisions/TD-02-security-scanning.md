# TD-02 — Security scanning: Semgrep, Trivy, OWASP ZAP

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Three different kinds of security testing are needed: static code analysis, dependency and image checks, and black-box testing of the running app.

## Decision

Semgrep (community edition) for static code scanning, Trivy for dependencies, images, infrastructure code and secrets, and OWASP ZAP for black-box testing after deployment. All behind adapters.

## Consequences

Semgrep and Trivy run on every PR in the MVP; ZAP follows after the MVP. Community MCP servers exist for these tools but are checked for maturity before use.

## Related

[Delivery and testing](../requirements/delivery-and-testing.md), [Identity and security](../requirements/identity-and-security.md), [Tools and adapters](../tools-and-adapters.md)
