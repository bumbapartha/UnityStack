# UnityStack

### Enterprise AI Software Factory

**From Requirements to Production. Powered by AI. Governed by Humans.**

---

## Overview

**UnityStack** is an AI-first, self-hostable software delivery platform that helps organisations plan, design, build, test, deploy and operate software while retaining human governance and their existing development practices.

UnityStack combines a conversational AI workspace, live artifact previews, Git-based review, workflow orchestration, and an integrated view of the software delivery lifecycle. It can actively help teams deliver new functionality or passively learn from work completed entirely in existing tools.

> **AI-first, not AI-only.** Teams can continue to use their IDEs, GitHub, project-management tools and CI/CD pipelines independently of UnityStack.

## Product principles

- **Conversation-first creation:** Business analysts, architects, developers and testers collaborate with AI to propose artifacts rather than filling out traditional creation forms.
- **Two-pane workspace:** AI conversation on the left; context-specific, read-only live preview and changes on the right. Future versions may open artifacts in web or desktop editors.
- **Human-governed delivery:** Users review proposals and create pull requests; additional reviewer approvals are configurable by organisation. Mandatory validation and security policies still apply.
- **Git-first approved artifacts:** Approved requirements, architecture, code, tests and configuration are versioned and reviewable in Git.
- **Traditional SDLC remains valid:** Developers can create stories, commits and PRs and deliver releases through existing tools without opening UnityStack.
- **Traceability across delivery:** Connect requirements, epics, stories, decisions, code changes, tests, releases, incidents and operational feedback.
- **Continuous delivery from day one:** Establish a deployable walking skeleton (even Hello World), then evolve it through small, governed releases.

## How it works

### AI-assisted delivery

1. Discuss a feature or change with the appropriate AI agent.
2. Review evolving BRDs, epics, stories, architecture, code or tests in a live preview with revision diffs.
3. Finalise the proposal and create a GitHub pull request.
4. Apply organisation-defined review and approval policies.
5. Merge approved changes; synchronise artifacts with connected tools and advance eligible work.
6. Validate and deploy through CI/CD, with release policies and production monitoring.

### Existing-tool delivery

A team may independently create work items in OpenProject or Jira, develop in its preferred IDE, create PRs in GitHub, and deploy through existing pipelines. UnityStack listens to supported events, reconciles source APIs, and builds a searchable historical record without blocking that workflow.

## Enterprise adoption

UnityStack is designed for two onboarding models:

| Model | Experience |
| --- | --- |
| **Greenfield** | Deploy UnityStack and a supported tool stack into the organisation's AWS environment. |
| **Existing organisation** | Connect existing SDLC systems, ingest accessible history, reconstruct links, and enable ongoing synchronisation. |

The long-term vision is **Bring Your Own Tool (BYOT)**: each organisation can choose a supported default, connect an existing system, or provision another supported tool where possible. This is a future capability, not a promise of full MVP support.

Initial historical ingestion and ongoing synchronisation are separate processes. UnityStack uses webhooks, API reconciliation, resumable imports and source identifiers to minimise gaps. Reconstruction depends on the data retained and exposed by each source system.

## Architecture at a glance

| Layer | Responsibility |
| --- | --- |
| **Vue 3 workspace** | AI chat, role-specific live preview, diffs, finalisation and review surfaces. |
| **Spring Boot control plane** | Identity, policy, artifact metadata, orchestration state, approvals, integrations and audit. |
| **AI orchestration** | Agent workflows for business analysis, architecture, development and testing; model-agnostic routing is planned. |
| **Tool adapters and ingestion** | Connectors, webhooks, API reconciliation, historical import and normalised SDLC records. |
| **GitHub** | Authoritative repository, branch, commit, PR and approved Git-artifact state. |
| **UnityStack state store** | Authoritative AI session, draft revision, assignment and workflow state. |
| **CI/CD and observability** | Build, test, release, deployment and operational feedback from their respective systems. |

**Source-system ownership:** Each connected tool remains authoritative for its native records. UnityStack retains source provenance and distinguishes verified links from AI-inferred associations.

## Selected open-source ecosystem

The initial selected tool stack is:

| Capability | Tool |
| --- | --- |
| Project management | [OpenProject](https://github.com/opf/openproject) |
| Wireframing | [Excalidraw](https://github.com/excalidraw/excalidraw) |
| UX design | [Penpot](https://github.com/penpot/penpot) |
| Architecture diagrams | [Mermaid](https://github.com/mermaid-js/mermaid) |
| Documentation | [MkDocs](https://github.com/mkdocs/mkdocs) |
| Software catalog | [Backstage](https://github.com/backstage/backstage) |
| API documentation | [Swagger UI](https://github.com/swagger-api/swagger-ui) |
| Browser IDE | [Eclipse Theia](https://github.com/eclipse-theia/theia) |
| Code quality | [SonarQube Community Build](https://github.com/SonarSource/sonarqube) |

These tools have different deployment and integration models. Availability in the platform does not imply every tool is fully AI-integrated on day one.

## Packaging and deployment

The target is a **versioned Enterprise AI Software Factory in a Box** distribution:

- **AWS-first** deployment using Terraform for infrastructure provisioning.
- Container-based installation of UnityStack and supported services, with independently managed persistent data.
- Organisation configuration, secrets management, installation validation, health checks, backups and upgrade paths.
- A single-tenant installation per organisation for the initial architecture, supporting multiple teams, projects and repositories.
- A future enterprise onboarding mode for connecting existing systems instead of provisioning replacements.

The MVP may use EC2 and Docker Compose to reduce initial complexity; an enterprise-grade Kubernetes deployment is a future option. Capacity must be validated for the complete tool stack.

## MVP focus

The first implementation aims to prove the complete delivery path, rather than automate every SDLC tool at once:

1. Install a usable AWS-hosted UnityStack environment and selected tools.
2. Provide the AI chat + live preview experience for an initial requirements workflow.
3. Generate structured artifacts, show revisions, and create GitHub PRs after user finalisation.
4. Support configurable review policies and safe merge handling.
5. Synchronise initial GitHub and OpenProject activity, including changes made outside UnityStack.
6. Link work items, PRs and deployment evidence using stable identifiers.
7. Demonstrate a Hello World application delivered through CI/CD, then incrementally add a feature.

**Not part of the initial MVP:** full BYOT configuration, advanced historical ingestion across every vendor, native editing of all artifacts, comprehensive visual diffs, or autonomous production changes without trusted controls.

## Traceability

A stable business change identifier can connect:

`Requirement → Epic / Story → Architecture Decision → PR / Commit → Tests → Release → Deployment → Incident / Fix`

This business change ID is distinct from an application runtime distributed-tracing ID. Missing relationships can be proposed by AI but should remain labelled as inferred until verified.

## Run locally (first slice: Project Inception)

The first implemented step is **Project Inception** ([Requirements §22](Requirements.md)): click **New Project**, answer the AI's questions, watch the Project Charter build up on the right, then confirm to create the project.

Requirements: Docker Desktop.

```bash
cp .env.example .env          # optional; defaults to the scripted mock AI
docker compose up --build
```

Open http://localhost:8080.

To use a real model, set in `.env`:

```bash
UNITYSTACK_AI_PROVIDER=openai
OPENAI_API_KEY=sk-...
OPENAI_MODEL=gpt-4.1-mini
```

| Path | Contents |
| --- | --- |
| `frontend/` | Vue 3 + Vite workspace (served by nginx in Docker) |
| `backend/` | Spring Boot control plane, AI adapter (mock/OpenAI), Flyway migrations |
| `docker-compose.yml` | PostgreSQL + backend + frontend |

PostgreSQL is reachable from your machine at `localhost:5433` (database/user/password `unitystack`; change the port with `UNITYSTACK_DB_PORT`). Data persists in the `pgdata` Docker volume; `docker compose down -v` deletes it.

## Project status

**Status: Planning / requirements definition.** The architecture and tool choices described here represent the intended product direction; the full installable platform and integrations are not claimed to be implemented yet.

## Vision

**UnityStack — Enterprise AI Software Factory**  
*From Requirements to Production. Powered by AI. Governed by Humans.*
