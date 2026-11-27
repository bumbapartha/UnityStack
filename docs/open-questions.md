# Open questions and risks

[← Documentation index](README.md)

When a question is settled, add a decision file in [decisions](decisions/README.md) and remove the row here.

## Open questions

| ID | Decision | Current state |
|---|---|---|
| D-4 | Change ID format and how it is carried | For example `<PROJECTKEY>-CHG-001`, in branch names, commit trailers, PR labels, build metadata and log fields. |
| D-5 | First project-management provider in the loop | OpenProject is the default; Jira follows as the second adapter. |
| D-7 | Review policy for a sole user | GitHub does not let an author approve their own PR. Decide on self-merge rules for the sole-user phase. |
| D-8 | OpenProject sync direction | Git-to-tool for approved definitions with field-level ownership ([Coexistence and sync](requirements/coexistence-and-sync.md)); controlled tool-to-PR proposals later. |
| D-9 | Penpot export fidelity and authoritative design format | Later (P2). |
| D-10 | Tool versions, licensing and distribution rights | Verify before packaging (P2). |
| D-11 | Adapter, event envelope and schema contracts | Define in HLD; the event envelope includes `eventId`, `eventType`, `occurredAt`, `organisationId`, `projectId`, `correlationId`, `changeId`, `actor`, `artifactRefs`, `schemaVersion`. |
| D-12 | SSO for third-party tools | Keycloak is decided for UnityStack ([AD-3](decisions/AD-3-keycloak-now.md)). Which tools join Keycloak SSO, and how (OIDC or SAML), is decided per tool. |
| D-13 | Scale, SLOs, RTO and RPO | After baseline measurement. |
| D-17 | MCP servers for tools | Community MCP servers exist for some tools (for example the security scanners); check maturity before adapters depend on them. |
| D-19 | Order of further Git providers | Proposed: Gitea first (keeps a fully open-source option), then GitLab and Bitbucket. |
| D-18 | Meeting platform for the AI meeting assistant | Later (P2); candidates Jitsi Meet or Mattermost calls, with Zoom and Teams adapters. |

## Implementation proposals to validate

- PostgreSQL and SSE.
- Terraform + EC2/Docker Compose as the first AWS deployment mechanism.
- Change ID format (D-4).
- Specific release numbering, scope and sequencing.
- Event broker, model router and operational SLOs.

## Product risks

| Risk | Mitigation |
|---|---|
| Building every stage deeply before the loop works end to end | Thin loop first ([Scope and roadmap](roadmap.md)), then deepen stage by stage |
| Adapter interfaces shaped around one provider | Design interfaces from capabilities; add a second provider (Jira) early |
| AI corrupts approved documents | Baseline SHA, schema checks, read-only preview, explicit finalisation, PRs |
| Agents get excessive permissions | Trusted execution layer, least privilege, sandboxing, human approval |
| Prompt injection through logs or repository content | Treat all retrieved content as untrusted data; no execution authority from it |
| GitHub and UnityStack drift | Webhooks plus periodic reconciliation; GitHub stays authoritative |
| Secrets committed by mistake | Placeholders only in `.env.example`; GitHub push protection; secret scanning |
| LLM and cloud costs grow | Mock provider for development; spending limits; small footprint |
| Faulty changes deployed | Tests, protected branches, health checks, rollback, feature flags |
| Tool licensing or SSO limits | Verify per tool before packaging |
| Solo capacity | Keep scope to the thin loop; defer P2 entirely |
| Laptop memory with many tools | Compose profiles; move heavy tools to one EC2 host early if needed |
| Community MCP servers or tool editions change | Adapters call tools directly by default; MCP only after a maturity check |

## Delivery risks

| Risk | Mitigation |
|---|---|
| Attempting all tools deeply at once | Thin loop first with one provider per capability; deepen and package later |
| Laptop memory with many containers | Compose profiles; move heavy tools to one EC2 host early if needed |
| AI corrupts approved docs | Baseline SHA, schema checks, read-only preview, user finalisation and Git PR |
| Agents receive excessive permissions | Trusted execution layer, least privilege, sandbox, human governance |
| GitHub and UnityStack drift | Webhooks plus periodic reconciliation; GitHub remains authoritative |
| External SDLC records lack links | Preserve provenance; infer cautiously; show unresolved relationships |
| AWS cost rises | Small initial footprint, resource budgets, stop nonessential services |
| CI/CD deploys faulty changes | Tests, protected branches, health checks, rollback and feature flags |
| Circular dependency on unfinished modules | Bootstrap each module conventionally, then use it to design the next |
| Tool licensing or SSO limitations | Validate deployment rights and edition-specific capabilities before packaging |
