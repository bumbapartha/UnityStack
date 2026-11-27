# UnityStack — guidelines for AI assistants and contributors

UnityStack is an AI-first, open-source, self-hosted software delivery platform: one closed loop from requirement to production feedback, joined by a Change ID, with every tool behind an adapter. Read this file first; `backend/CLAUDE.md` and `frontend/CLAUDE.md` add the rules for each side.

## Where things are

| Need | Read |
|---|---|
| What to build and why | `docs/requirements/` — one file per SDLC phase, plus cross-cutting files; ID index in `docs/requirements/README.md` |
| What is in the MVP and in which phase | `docs/roadmap.md` |
| Settled decisions (PD, AD, TD) | `docs/decisions.md` |
| Code layout and module rules | `docs/architecture.md` → "Code organisation: feature modules" |
| Default tool per capability | `docs/tools-and-adapters.md` |
| Undecided items | `docs/open-questions.md` — do not silently decide these; ask |

Before changing behaviour, find the requirement (FR-/NFR- ID) it implements. If none exists, propose one in `docs/requirements/` in the same PR.

## Working rules

1. **Branch and PR only.** Never commit to `main` directly. One logical change per PR.
2. **Small PRs: at most 200 changed lines.** Additions plus deletions, as GitHub counts them, must not exceed 200 per PR. If a request needs more, say so **before starting**: give an estimate and propose how to split it into a series of small PRs, each reviewable and working on its own. Then wait for agreement. Never open one large PR. Write generated files compactly; for example, Excalidraw files use one element per line.
3. **Reference IDs.** Commit messages and PR descriptions name the requirement and decision IDs they implement (for example `FR-105`, `AD-5`). Once the Change ID scheme exists (open question D-4), carry it in branch names, commits and PRs too.
4. **Adapters everywhere (PD-2, PD-5).** Never call a tool's API directly from workflow or agent code. Add or use an adapter in the module that owns the capability. Swapping a provider must only touch its adapter and configuration.
5. **Feature modules (AD-5).** Code is grouped by feature module (requirements, design, development, testing, deployment, monitoring, security, communication, traceability) plus a small `core`. Never group code by file type across the app.
6. **AI proposes, humans govern.** AI output is validated by trusted code before any action. AI never merges, deploys to production or creates tracker items without the required human action.
7. **Secrets.** Real keys live only in `.env` (git-ignored). `.env.example` holds placeholders only. Never log, commit or send secrets to a model.
8. **Open source first.** Default dependencies and tools must be open source. Flag any licence that is not (for example AGPL or source-available) in the PR.
9. **Keep docs in step.** If a change alters behaviour, a decision or the module layout, update the matching file under `docs/` in the same PR.

## Local development (TD-10)

- Start the everyday stack: `docker compose up --build` (Compose profiles such as `core` and `monitoring` are planned; see `docs/dev-setup.md`).
- **After finishing a batch of edits** — not after each file — rebuild only what changed: `docker compose up --build -d backend` or `docker compose up --build -d frontend`.
- For manual editing, `docker compose watch` can rebuild or sync automatically once `develop.watch` rules are added to `docker-compose.yml`.
- CI runs tests and security scans on every PR; it does not deploy UnityStack itself while development is local. Projects that UnityStack manages do deploy to their own environments (AD-6).
- Run the relevant tests before opening a PR (see the backend and frontend guidelines).

## Current state

Only Project Inception is built. The code still uses the old package layout (`inception/`, `project/`, `ai/`, `common/` and `views/`). Moving it to feature modules is a planned Phase 0 step in `docs/roadmap.md`. Until then, put **new** code in the target layout and leave the existing code where it is, unless the task is the restructure itself.
