# Development setup

[← Documentation index](README.md)

How to run UnityStack locally today is in the [root README](../README.md#run-locally-first-slice-project-inception). This page describes the target local setup and the move to AWS.

## Runtime environments

This page is about running **UnityStack itself**. The projects UnityStack manages deploy to their own environments on AWS from the MVP ([AD-6](decisions.md#ad-6)).

**Now — local Docker Compose.** Every tool runs as a container from its official image; nothing is installed on the developer's machine. Services are grouped with Docker Compose profiles so only what is needed starts, for example:

| Profile          | Services                                                | Status               |
| ---------------- | ------------------------------------------------------- | -------------------- |
| core, no profile | PostgreSQL, backend, frontend                           | Built; Keycloak next |
| `pm`             | OpenProject                                             | Phase 2              |
| `test`           | Kiwi TCMS                                               | Phase 3              |
| `monitoring`     | OpenTelemetry Collector, Grafana, Loki, Prometheus      | Phase 4              |
| `oncall`         | GoAlert                                                 | After the MVP        |
| `chat`           | Mattermost                                              | After the MVP        |
| `flags`          | Unleash                                                 | After the MVP        |
| `design`         | Penpot (Excalidraw files open in the VS Code extension) | With UX design (MVP) |

The core services have no `profiles` key, so they always start: `docker compose up --build` runs the everyday stack, as before. Each optional tool joins one profile when its service is added to `docker-compose.yml`, and starts only when its profile is named, for example `docker compose --profile pm --profile monitoring up`. To start the same profiles every time, set `COMPOSE_PROFILES=pm,monitoring` in `.env`. Memory use is the main constraint on a laptop.

**Local rebuilds (no CI deployment).** During local development nothing is deployed by CI. Instead:

- **When AI edits code:** after finishing a batch of edits (not after each file), the AI runs `docker compose up --build -d <service>` to rebuild and restart only the changed service (or the whole stack without a service name). This avoids repeated restarts while files are still changing.
- **When editing by hand:** `docker compose watch` watches the source folders; it syncs frontend changes straight into the running container and rebuilds the backend only when backend files change.
- GitHub Actions still runs tests and security scans on every PR of UnityStack, but does not deploy UnityStack.

**Formatting and lint.** Spotless (Palantir Java Format) formats the backend and Prettier formats the frontend and Markdown; `.editorconfig` sets the basics for every editor. VS Code formats on save with the extensions recommended in `.vscode/extensions.json` (Java format on save needs a local JDK 21). CI fails a PR on unformatted code. Without local tools, run the formatters in Docker before committing:

```bash
# Backend, from backend/
docker run --rm -v "$PWD":/src -v unitystack-gradle-cache:/home/gradle/.gradle -w /src gradle:9.9.2-jdk21-alpine gradle spotlessApply
# Frontend, from the repository root (Prettier reads the root .prettierrc.json)
docker run --rm -v "$PWD":/repo -w /repo/frontend node:22.11-alpine sh -c "corepack enable && yarn install && yarn format && yarn lint"
```

**Security scans (TD-02, FR-117).** The CI `security` job runs Semgrep (community edition, open-source rules for Java, JavaScript/Vue, Dockerfiles and secrets) and Trivy (repository filesystem: Gradle and Yarn dependencies, Dockerfiles, secrets; plus the backend and frontend images). All findings appear in GitHub's Security tab; the job fails on HIGH or CRITICAL. Run the same scans locally from the repository root:

```bash
# Semgrep. On Apple Silicon, add --platform linux/amd64 if the native image fails to start.
docker run --rm -v "$PWD":/src -w /src semgrep/semgrep:1.187.0 semgrep scan --metrics=off \
  --config p/java --config p/javascript --config p/dockerfile --config p/secrets
# Trivy filesystem. Always skip .env: it holds real keys, and a secret finding prints part of the value.
docker run --rm -v "$PWD":/src -v trivy-cache:/root/.cache/trivy aquasec/trivy:0.76.2 fs \
  --scanners vuln,secret,misconfig --severity HIGH,CRITICAL --skip-files /src/.env \
  --skip-dirs /src/frontend/node_modules,/src/backend/build,/src/backend/.gradle /src
# Trivy images: build first, then scan each image (the first run downloads the Java database).
docker compose build backend frontend
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy-cache:/root/.cache/trivy \
  aquasec/trivy:0.76.2 image --timeout 30m --severity HIGH,CRITICAL unitystack-backend
```

Trivy reads Gradle dependencies only from `backend/gradle.lockfile`; keep it current (see the backend guidelines). Suppress a finding only with a one-line reason: in `.trivyignore`, or a `nosemgrep: <rule-id>` comment next to the code.

**Next — AWS, as soon as the thin loop works.** The same Compose files run on EC2 with minimal change, provisioned by Terraform. Allowed middle step: move only the heavy tools (for example monitoring, chat, test management) to one EC2 host while the app runs locally. Stop the instance when not in use to control cost. From here CI/CD deploys: each PR to a preview environment, merges to main to the main (production) environment ([FR-129](requirements/05-deployment.md)). Images go to GitHub Container Registry, with the ECR adapter added alongside.

**Later — enterprise.** See [Distribution and onboarding](requirements/11-distribution-and-onboarding.md).
