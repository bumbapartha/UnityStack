# Vision and principles

[← Documentation index](README.md)

## Product summary

UnityStack is an AI-first, Git-driven software delivery platform. It covers the whole lifecycle — requirements, planning, code and tests, review, deployment, monitoring and feedback — and links every step with one shared change identifier.

People work with AI through a two-pane workspace: a conversation on the left and a live, read-only preview of the artifact on the right. The AI proposes; a person finalises; GitHub pull requests and policy govern what becomes approved. Teams can also work entirely in their own tools, and UnityStack observes and links that work without blocking it.

Every external tool, and the AI model itself, sits behind an adapter, so any of them can be swapped without changing the product.

**Guiding statement:** *From requirements to production. Powered by AI. Governed by humans.*

## Product decisions

- [PD-1](decisions.md#pd-1) Primary user
- [PD-2](decisions.md#pd-2) Adapters everywhere
- [PD-3](decisions.md#pd-3) Product core: the closed loop
- [PD-4](decisions.md#pd-4) Thin loop first
- [PD-5](decisions.md#pd-5) Open-source defaults
- [PD-6](decisions.md#pd-6) A complete MVP

All decisions: [Decisions](decisions.md).

## Product principles

1. **AI-first, not AI-only.** UnityStack-native artifacts are created and revised through AI conversation; there are no manual authoring forms in the MVP. Editing in external tools, IDEs and GitHub is always allowed.
2. **AI proposes, humans govern.** The user explicitly finalises every AI proposal. GitHub branch protection, required checks and policy decide what merges and deploys. A successful AI response or test run is never a substitute for required human approval.
3. **The Git provider owns Git state.** The connected Git provider (GitHub first) is authoritative for repositories, branches, commits, PRs, reviews and merged artifacts. Each connected tool is authoritative for its own native records. UnityStack owns conversations, drafts, orchestration state, indexes and trace links.
4. **Adapters everywhere.** Workflows and agents call logical capabilities (for example `projectManagement.createStory`), never a provider's API directly. The LLM is a capability like any other.
5. **Your data stays yours.** Context lives in UnityStack's store, Git and the connected tools — never only inside a model provider. Switching model means the new model is fed the same context from those stores.
6. **One ID through the loop.** A stable Change ID links requirement, story, ADR, branch, commit, PR, test, build, deployment, log evidence and bug.
7. **Traditional SDLC remains valid.** Work done entirely outside UnityStack is observed and linked, never blocked.
8. **Walking skeleton and continuous delivery.** A deployable Hello World comes first; everything grows through small governed changes.
9. **Everything durable as code.** Requirements, ADRs, diagrams, API specs, code, tests, pipelines and infrastructure are Git artifacts. Runtime state, logs, metrics, secrets and tool databases are not.
10. **One reusable workspace, many modules.** Chat, preview, diff, revision, finalise and PR mechanics are shared; agents and renderers vary by role.

## Users and roles

**Now:** one person (the founder) plays every role below, using UnityStack to build UnityStack.

**Later:** each role can be a different person, and an organisation can require that the reviewer is not the author.

| Role | Responsibility |
|---|---|
| Organisation administrator | Configure UnityStack, connected tools, policies and access |
| Product owner / business analyst | Shape requirements, epics, stories and priorities |
| UX designer | Review wireframes and UX artifacts |
| Solution architect | Review architecture, ADRs and API contracts |
| Developer | Discuss requirements, review and implement code and tests |
| Reviewer | Review and approve PRs |
| QA engineer | Review tests and quality evidence |
| DevOps / SRE | Manage pipelines, environments, releases, monitoring and incidents |
| AI agent | Propose artifacts and actions only within granted permissions |
