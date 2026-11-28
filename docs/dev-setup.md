# Development setup

[← Documentation index](README.md)

How to run UnityStack locally today is in the [root README](../README.md#run-locally-first-slice-project-inception). This page describes the target local setup and the move to AWS.

## Runtime environments

This page is about running **UnityStack itself**. The projects UnityStack manages deploy to their own environments on AWS from the MVP ([AD-6](decisions.md#ad-6)).

**Now — local Docker Compose.** Every tool runs as a container from its official image; nothing is installed on the developer's machine. Services are grouped with Docker Compose profiles so only what is needed starts, for example:

| Profile | Services |
|---|---|
| `core` | PostgreSQL, backend, frontend, Keycloak |
| `pm` | OpenProject |
| `test` | Kiwi TCMS |
| `monitoring` | Grafana, Loki, Prometheus |
| `oncall` | GoAlert |
| `chat` | Mattermost |
| `flags` | Unleash |
| `design` | Penpot (Excalidraw files open in VS Code or the Excalidraw container) |

`docker compose --profile core up` starts the everyday stack; adding `--profile monitoring` and others brings in the rest. Memory use is the main constraint on a laptop.

**Local rebuilds (no CI deployment).** During local development nothing is deployed by CI. Instead:

- **When AI edits code:** after finishing a batch of edits (not after each file), the AI runs `docker compose up --build -d <service>` to rebuild and restart only the changed service (or the whole stack without a service name). This avoids repeated restarts while files are still changing.
- **When editing by hand:** `docker compose watch` watches the source folders; it syncs frontend changes straight into the running container and rebuilds the backend only when backend files change.
- GitHub Actions still runs tests and security scans on every PR of UnityStack, but does not deploy UnityStack.

**Formatting and lint.** Spotless (Palantir Java Format) formats the backend and Prettier formats the frontend and Markdown; `.editorconfig` sets the basics for every editor. VS Code formats on save with the extensions recommended in `.vscode/extensions.json` (Java format on save needs a local JDK 21). CI fails a PR on unformatted code. Without local tools, run the formatters in Docker before committing:

```bash
# Backend, from backend/
docker run --rm -v "$PWD":/src -v unitystack-gradle-cache:/home/gradle/.gradle -w /src gradle:8.10.2-jdk21 gradle spotlessApply
# Frontend, from the repository root (Prettier reads the root .prettierrc.json)
docker run --rm -v "$PWD":/repo -w /repo/frontend node:22.11-alpine sh -c "npm install && npm run format && npm run lint"
```

**Next — AWS, as soon as the thin loop works.** The same Compose files run on EC2 with minimal change, provisioned by Terraform. Allowed middle step: move only the heavy tools (for example monitoring, chat, test management) to one EC2 host while the app runs locally. Stop the instance when not in use to control cost. From here CI/CD deploys: each PR to a preview environment, merges to main to the main (production) environment ([FR-129](requirements/05-deployment.md)). Images go to GitHub Container Registry, with the ECR adapter added alongside.

**Later — enterprise.** See [Distribution and onboarding](requirements/11-distribution-and-onboarding.md).
