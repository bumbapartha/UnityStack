# Tools and adapters

[← Documentation index](README.md)

Every capability is reached through an adapter ([Core platform](requirements/10-core-platform.md#adapters-and-capability-registry)). The default provider is open source; alternatives can be added as further adapters. **MVP** column: Yes = MVP thin loop; P1 = straight after; P2 = later.

## Source control

| Capability             | Default (open source)  | Alternatives                                                    | MVP |
| ---------------------- | ---------------------- | --------------------------------------------------------------- | --- |
| Source control and PRs | GitHub (first adapter) | Gitea (open source), GitLab, Bitbucket, Azure DevOps Repos (P2) | Yes |

**Requirements:** [Development phase](requirements/03-development.md), [Traceability and integration](requirements/09-traceability-and-integration.md)  
**Decisions:** [AD-4](decisions.md#ad-4)

## AI models

| Capability | Default (open source)       | Alternatives                                                       | MVP |
| ---------- | --------------------------- | ------------------------------------------------------------------ | --- |
| LLM        | Configurable (mock, OpenAI) | Any provider via the adapter; routing gateway such as LiteLLM (P1) | Yes |

**Requirements:** [Core platform](requirements/10-core-platform.md)  
**Decisions:** [PD-2](decisions.md#pd-2), [AD-1](decisions.md#ad-1)

## Identity and access

| Capability          | Default (open source) | Alternatives                                                              | MVP |
| ------------------- | --------------------- | ------------------------------------------------------------------------- | --- |
| Identity and access | Keycloak              | Azure AD / Entra ID, Okta through Keycloak federation or own adapter (P2) | Yes |

**Requirements:** [Security](requirements/07-security.md)  
**Decisions:** [AD-3](decisions.md#ad-3)

## Project management

| Capability                                | Default (open source) | Alternatives          | MVP |
| ----------------------------------------- | --------------------- | --------------------- | --- |
| Project management (epics, stories, bugs) | OpenProject           | Jira (P1), Plane (P2) | Yes |

**Requirements:** [Requirements phase](requirements/01-requirements.md), [Monitoring and operations phase](requirements/06-monitoring-and-operations.md), [Traceability and integration](requirements/09-traceability-and-integration.md)

## CI/CD

| Capability | Default (open source) | Alternatives          | MVP |
| ---------- | --------------------- | --------------------- | --- |
| CI/CD      | GitHub Actions        | Jenkins, Argo CD (P2) | Yes |

**Requirements:** [Deployment phase](requirements/05-deployment.md)  
**Decisions:** [TD-01](decisions.md#td-01), [TD-10](decisions.md#td-10)

## Container registry

| Capability                    | Default (open source)     | Alternatives            | MVP                    |
| ----------------------------- | ------------------------- | ----------------------- | ---------------------- |
| Artifact (container) registry | GitHub Container Registry | Amazon ECR, Harbor (P2) | Yes (managed projects) |

**Requirements:** [Deployment phase](requirements/05-deployment.md)  
**Decisions:** [TD-04](decisions.md#td-04)

## Security scanning

| Capability                                       | Default (open source)       | Alternatives                                 | MVP |
| ------------------------------------------------ | --------------------------- | -------------------------------------------- | --- |
| Static code security scanning (SAST)             | Semgrep (community edition) | SonarQube security rules, others (P2)        | Yes |
| Dependency, image, IaC and secret scanning (SCA) | Trivy                       | OWASP Dependency-Check, GitHub scanning (P2) | Yes |
| Dynamic / black-box security testing (DAST)      | OWASP ZAP                   | —                                            | P1  |

**Requirements:** [Security](requirements/07-security.md)  
**Decisions:** [TD-02](decisions.md#td-02)

## Testing

| Capability                             | Default (open source)      | Alternatives | MVP |
| -------------------------------------- | -------------------------- | ------------ | --- |
| Unit tests                             | JUnit (Java), Vitest (Vue) | —            | Yes |
| End-to-end tests                       | Playwright                 | —            | Yes |
| Test management (cases, runs, reports) | Kiwi TCMS                  | —            | Yes |

**Requirements:** [Testing phase](requirements/04-testing.md), [Development phase](requirements/03-development.md)  
**Decisions:** [TD-03](decisions.md#td-03)

## Observability

| Capability                 | Default (open source)                       | Alternatives                                                          | MVP                           |
| -------------------------- | ------------------------------------------- | --------------------------------------------------------------------- | ----------------------------- |
| Telemetry collection       | OpenTelemetry (Java agent, SDKs, Collector) | —                                                                     | Yes                           |
| Logs                       | Loki                                        | Elasticsearch with Kibana, OpenSearch with OpenSearch Dashboards (P2) | Yes                           |
| Metrics                    | Prometheus                                  | —                                                                     | Yes                           |
| Dashboards and alert rules | Grafana (Grafana alerting)                  | Prometheus Alertmanager (P2)                                          | Yes (dashboards); P1 (alerts) |

**Requirements:** [Monitoring and operations phase](requirements/06-monitoring-and-operations.md), [Traceability and integration](requirements/09-traceability-and-integration.md)  
**Decisions:** [TD-05](decisions.md#td-05), [TD-06](decisions.md#td-06)

## Alerting and on-call

| Capability                    | Default (open source) | Alternatives                        | MVP |
| ----------------------------- | --------------------- | ----------------------------------- | --- |
| On-call paging and escalation | GoAlert               | Opsgenie, PagerDuty, OneUptime (P2) | P1  |

**Requirements:** [Monitoring and operations phase](requirements/06-monitoring-and-operations.md)  
**Decisions:** [TD-06](decisions.md#td-06), [TD-07](decisions.md#td-07)

## Feature flags

| Capability    | Default (open source)    | Alternatives                                                     | MVP |
| ------------- | ------------------------ | ---------------------------------------------------------------- | --- |
| Feature flags | OpenFeature with Unleash | Flagsmith or an organisation's own provider via OpenFeature (P2) | P1  |

**Requirements:** [Deployment phase](requirements/05-deployment.md)  
**Decisions:** [TD-08](decisions.md#td-08)

## Notifications

| Capability                  | Default (open source) | Alternatives                | MVP |
| --------------------------- | --------------------- | --------------------------- | --- |
| Notifications and team chat | Mattermost            | Slack, Microsoft Teams (P2) | P1  |

**Requirements:** [Communication](requirements/08-communication.md)  
**Decisions:** [TD-09](decisions.md#td-09)

## Meetings

| Capability                      | Default (open source)                                        | Alternatives     | MVP |
| ------------------------------- | ------------------------------------------------------------ | ---------------- | --- |
| Meetings (AI meeting assistant) | _To be selected, for example Jitsi Meet or Mattermost calls_ | Zoom, Teams (P2) | P2  |

**Requirements:** [Communication](requirements/08-communication.md)  
**Decisions:** [TD-11](decisions.md#td-11)

## Design, documentation and developer tools

| Capability              | Default (open source)     | Alternatives       | MVP |
| ----------------------- | ------------------------- | ------------------ | --- |
| Architecture diagrams   | Mermaid (library)         | —                  | Yes |
| Technical documentation | MkDocs                    | Confluence (P2)    | Yes |
| API documentation       | Swagger UI                | —                  | Yes |
| Code quality            | SonarQube Community Build | —                  | P1  |
| Initial wireframes      | Excalidraw                | —                  | Yes |
| UX/UI design            | Penpot                    | —                  | Yes |
| Software catalogue      | Backstage                 | —                  | P2  |
| Web IDE                 | Eclipse Theia             | Local IDE (always) | P2  |

**Requirements:** [Architecture and design phase](requirements/02-architecture-and-design.md), [Testing phase](requirements/04-testing.md)

## Notes

The nine originally selected tools are OpenProject, Excalidraw, Penpot, Mermaid, MkDocs, Backstage, Swagger UI, Eclipse Theia and SonarQube Community Build. On 27 November 2026 the default set was extended to cover the remaining SDLC gaps: identity, CI/CD, artifact registry, security scanning, testing, observability, alerting and on-call, feature flags and notifications. All defaults are open source; Semgrep and Unleash have paid editions whose extra features are not required.

Notes on how the new tools fit together:

- **Semgrep vs Trivy:** Semgrep reads UnityStack's own code for risky patterns (for example injection or unsafe input handling). Trivy checks what the code depends on: library versions, container images, infrastructure misconfigurations and leaked secrets. Both are needed.
- **OpenTelemetry:** the open standard for logs, metrics and traces. The Java agent instruments Spring Boot requests, database and HTTP calls without code changes; custom spans can be added where needed. Existing logging (Logback, Log4j) keeps working: the agent adds the trace ID to each log line and sends logs on. Every signal carries `service.name`, `service.version` (release) and the Change ID. All telemetry goes through the **OpenTelemetry Collector**, so the backends behind it can change without touching application code.
- **Grafana, Loki, Prometheus:** Loki stores logs (indexing labels only, so it is light), Prometheus stores metrics, Grafana shows both, links a log line to its trace, and evaluates alert rules.
- **Changing the log store later (for example to Elasticsearch/Kibana or OpenSearch):** add or connect the new store; change the Collector configuration to send logs there (alongside Loki or instead of it); add a UnityStack log adapter for its query language; move log alert rules. Application code does not change, and Grafana can read Elasticsearch too.
- **Alert rules as code:** Grafana alerting rules are files in Git and are provisioned into Grafana at start-up, which makes them read-only in the Grafana UI; a change is a PR. As a safety net, a UnityStack sync job exports rules through Grafana's API and opens a PR if they have drifted ([FR-024](requirements/03-development.md)).
- **GoAlert:** on-call rotations (daily, weekly, hourly), temporary overrides and escalation policies; pages by phone call or SMS. Its API allows UnityStack to manage rotations later.
- **Kiwi TCMS:** test cases, test plans, test runs per build or release, and reports such as requirements without tests. Test cases are authored in Git (by AI, reviewed by PR); Kiwi keeps run history so every release has a permanent record of what was tested and what passed.
- **OpenFeature with Unleash:** Unleash is the flag server and dashboard; OpenFeature is the standard SDK the code calls. An organisation with its own flag system writes an OpenFeature provider and plugs it in without UnityStack code changes.
- **Viewing design artifacts in UnityStack (TD-13):** Excalidraw files render read-only in the preview pane straight from Git, Penpot designs appear as exports or an embedded prototype, and OpenAPI contracts are written in UnityStack and displayed with Swagger UI. Editing opens the tool through its adapter.

**Important:** an available provider does not imply full integration, SSO or feature parity. Each adapter documents its supported operations, versions, licensing and limitations.
