# TD-06 — Alert rules: Grafana alerting, rules kept in Git

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-14

## Context

Grafana alerting is simpler; Prometheus Alertmanager keeps rules in YAML but needs Loki's ruler for log alerts.

## Decision

Use Grafana alerting. Rules are files in Git, provisioned into Grafana and read-only in its UI. A sync job exports rules through Grafana's API and opens a PR if they drift.

## Consequences

Alert changes go through PR review like everything else.

## Related

[Observability and feedback](../requirements/observability-and-feedback.md), [Tools and adapters](../tools-and-adapters.md)
