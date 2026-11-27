# UnityStack documentation

UnityStack — Enterprise AI Software Factory. *From requirements to production. Powered by AI. Governed by humans.*

**Owner:** Partha Sarathi Ghosh · **Status:** Draft for review · **Version:** 2.2 (27 November 2026)

This folder replaces the single `Requirements.md` (v2.1) and the dogfooding development plan. Each topic has its own short file so a change touches one file per PR.

## Start here

| File | What it covers |
|---|---|
| [Vision and principles](vision.md) | What UnityStack is, product decisions, principles, users |
| [Scope and roadmap](roadmap.md) | MVP thin loop, what comes next, phases, backlog, acceptance |
| [Architecture](architecture.md) | The closed loop, components, current implementation, repositories |
| [Tools and adapters](tools-and-adapters.md) | Default tool per capability, alternatives, links to requirements |
| [Requirements](requirements/README.md) | Functional and non-functional requirements, one file per area, with an ID index |
| [Decisions](decisions/README.md) | One file per settled decision (PD, AD, TD) |
| [Open questions and risks](open-questions.md) | Undecided items and risks |
| [Development setup](dev-setup.md) | Docker Compose profiles, local rebuilds, move to AWS |
| [Document history](history.md) | Version history and where old sections went |

## Linking conventions

- Requirement IDs (FR-, NFR-), decision IDs (PD-, AD-, TD-) and open-question IDs (D-) are stable and never reused. Links use them, so they survive renames.
- Each requirement file lists the **tools** and **decisions** it depends on; each tool section lists the **requirements** and **decisions** that use it; each decision lists the files it affects.
- New requirements get the next free FR number and are added to the area file and the ID index.
- "GitHub" means the connected Git provider (GitHub is the first adapter, [AD-4](decisions/AD-4-git-provider-adapter.md)).

## Next documents

- System architecture and C4 diagrams
- Capability adapter contracts (including LLM, observability, identity, registry, test management, on-call and notifications)
- Change ID and traceability schema
- Git repository and artifact schema
- MVP user stories and acceptance tests for the thin loop
- Security threat model and runbooks
- Installation and upgrade specification (Later)
