# Development setup

[← Documentation index](README.md)

How to run UnityStack locally today is in the [root README](../README.md#run-locally-first-slice-project-inception). This page describes the target local setup and the move to AWS.

## Runtime environments

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

`docker compose --profile core up` starts the everyday stack; adding `--profile monitoring` and others brings in the rest. Memory use is the main constraint on a laptop.

**Local rebuilds (no CI deployment).** During local development nothing is deployed by CI. Instead:

- **When AI edits code:** after finishing a batch of edits (not after each file), the AI runs `docker compose up --build -d <service>` to rebuild and restart only the changed service (or the whole stack without a service name). This avoids repeated restarts while files are still changing.
- **When editing by hand:** `docker compose watch` watches the source folders; it syncs frontend changes straight into the running container and rebuilds the backend only when backend files change.
- GitHub Actions still runs tests and security scans on every PR, but does not deploy.

**Next — AWS, as soon as the thin loop works.** The same Compose files run on EC2 with minimal change, provisioned by Terraform. Allowed middle step: move only the heavy tools (for example monitoring, chat, test management) to one EC2 host while the app runs locally. Stop the instance when not in use to control cost. From here CI/CD deploys: each PR to a preview environment, merges to main to the main (production) environment ([FR-129](requirements/05-deployment.md)). Images go to GitHub Container Registry, with the ECR adapter added alongside.

**Later — enterprise.** See [Distribution and onboarding](requirements/11-distribution-and-onboarding.md).
