# TD-05 — Observability: OpenTelemetry with Grafana, Loki and Prometheus

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-3, D-15

## Context

None of the nine original tools covers logs and monitoring. The founder knows Grafana and Kibana.

## Decision

Instrument with OpenTelemetry (Java agent and Collector). Loki stores logs, Prometheus stores metrics, Grafana shows both. Elasticsearch with Kibana or OpenSearch can be added later as log adapters.

## Consequences

Logback/Log4j keep working with trace IDs in each line; signals carry service name, version and Change ID. Swapping the log store changes Collector config and one adapter, not app code. Loki is lighter than Elasticsearch but slower for full-text search at scale.

## Related

[Observability and feedback](../requirements/observability-and-feedback.md), [Traceability and orchestration](../requirements/traceability.md), [Tools and adapters](../tools-and-adapters.md)
