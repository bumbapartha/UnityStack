# UnityStack documentation

UnityStack — Enterprise AI Software Factory. *From requirements to production. Powered by AI. Governed by humans.*

**Owner:** Partha Sarathi Ghosh · **Status:** Draft for review · **Version:** 2.5 (27 November 2026)

This folder replaces the single `Requirements.md` (v2.1) and the dogfooding development plan.

## Start here

| File | What it covers |
|---|---|
| [Vision and principles](vision.md) | What UnityStack is, product decisions, principles, users |
| [Scope and roadmap](roadmap.md) | MVP thin loop, what comes next, phases, backlog, acceptance |
| [Requirements](requirements/README.md) | Requirements by SDLC phase, with an ID index |
| [Traceability](traceability.md) | How every item gets a permanent key and is traced from requirement to code, tests, release and bug |
| [Tools and adapters](tools-and-adapters.md) | Default tool per capability, alternatives, links to requirements |
| [Architecture](architecture.md) | The closed loop, components, current implementation, repositories |
| [Decisions](decisions.md) | Every settled decision in one table |
| [Open questions and risks](open-questions.md) | Undecided items and risks |
| [Development setup](dev-setup.md) | Docker Compose profiles, local rebuilds, move to AWS |
| [Wireframes](wireframes/README.md) | Excalidraw wireframes of UnityStack's screens, starting with the whole journey |
| [Document history](history.md) | Version history and where old sections went |

## Coding guidelines

The rules that people and AI assistants follow when writing code are in [`CLAUDE.md`](../CLAUDE.md) (whole repository), [`backend/CLAUDE.md`](../backend/CLAUDE.md) and [`frontend/CLAUDE.md`](../frontend/CLAUDE.md). Claude Code reads the file for the folder it is working in.

## Linking conventions

- Requirement IDs (FR-, NFR-), decision IDs (PD-, AD-, TD-) and open-question IDs (D-) are stable and never reused; links use them.
- Each requirement file lists the **tools** and **decisions** it depends on; each tool section lists the **requirements** and **decisions** that use it; each decision lists what it affects.
- New requirements get the next free FR number and go into the file for their SDLC phase.
- "GitHub" means the connected Git provider (GitHub is the first adapter, [AD-4](decisions.md#ad-4)).

## Next documents

- System architecture and C4 diagrams
- Capability adapter contracts (including LLM, observability, identity, registry, test management, on-call and notifications)
- Change ID and traceability schema
- Git repository and artifact schema
- MVP user stories and acceptance tests for the thin loop
- Security threat model and runbooks
- Installation and upgrade specification (Later)
