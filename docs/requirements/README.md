# Requirements

[← Documentation index](../README.md)

## How to read the requirements

- **Requirement IDs are stable.** Every [FR-001](distribution.md) to [FR-108](inception.md) ID from earlier versions is kept, so existing references stay valid. New requirements start at [FR-109](adapters.md). IDs are grouped by area, one file per area, rather than by the version that introduced them.
- **Priorities have been reset** to match the product decisions in [Decisions](../decisions/README.md) (see [Scope and roadmap](../roadmap.md) for what P0, P1 and P2 now mean).
- **"GitHub" means the connected Git provider.** GitHub is the first Git provider adapter ([AD-4](../decisions/AD-4-git-provider-adapter.md)). Where this document says GitHub, PR or GitHub App, read the equivalent in the connected provider (for example a GitLab merge request) unless it names a GitHub-only feature.
- **Detailed design** (C4 diagrams, API schemas, Terraform modules, threat models) belongs in separate HLD and ADR documents, linked from Git.
- [Inception](inception.md) is the only area currently specified at the slice level. Other areas will gain slice-level detail as they are built.
- Priorities marked *(Was …)* show the earlier priority where it changed.

### Priorities

Priorities in this document mean:

| Priority | Meaning |
|---|---|
| **P0 — MVP** | Needed for the thin closed loop, used by the sole user ([PD-1](../decisions/PD-1-primary-user.md), [PD-3](../decisions/PD-3-closed-loop.md)). |
| **P1 — Next** | Deepens the loop while dogfooding and prepares for a small group of demo users. |
| **P2 — Later** | Enterprise distribution and adoption: installer, full default-tool bundle, BYOT selection, SSO, HA, large-scale ingestion. |

## Areas

| Area | File | IDs |
|---|---|---|
| Adapters and capability registry | [adapters.md](adapters.md) | 8 |
| AI and LLM | [ai-and-llm.md](ai-and-llm.md) | 11 |
| AI workspace | [workspace.md](workspace.md) | 8 |
| Project Inception | [inception.md](inception.md) | 13 |
| Requirements and planning | [requirements-and-planning.md](requirements-and-planning.md) | 7 |
| Development and Git | [development-and-git.md](development-and-git.md) | 11 |
| Delivery and testing | [delivery-and-testing.md](delivery-and-testing.md) | 11 |
| Observability and feedback | [observability-and-feedback.md](observability-and-feedback.md) | 8 |
| Traceability and orchestration | [traceability.md](traceability.md) | 12 |
| Coexistence and sync | [coexistence-and-sync.md](coexistence-and-sync.md) | 18 |
| Identity and security | [identity-and-security.md](identity-and-security.md) | 3 |
| Design and documentation tools | [design-and-documentation.md](design-and-documentation.md) | 4 |
| Distribution and operations | [distribution.md](distribution.md) | 14 |
| AI meeting assistant | [meeting-assistant.md](meeting-assistant.md) | 1 |
| Non-functional requirements | [non-functional.md](non-functional.md) | 17 |

## ID index

| ID | Priority | Area |
|---|---|---|
| [FR-001](distribution.md) | P2 | Distribution and operations |
| [FR-002](distribution.md) | P2 | Distribution and operations |
| [FR-003](distribution.md) | P1 | Distribution and operations |
| [FR-004](distribution.md) | P2 | Distribution and operations |
| [FR-005](adapters.md) | P0 | Adapters and capability registry |
| [FR-006](distribution.md) | P2 | Distribution and operations |
| [FR-007](distribution.md) | P2 | Distribution and operations |
| [FR-008](distribution.md) | P1 | Distribution and operations |
| [FR-009](distribution.md) | P2 | Distribution and operations |
| [FR-010](distribution.md) | P2 | Distribution and operations |
| [FR-011](inception.md) | P0 | Project Inception |
| [FR-012](inception.md) | P0 | Project Inception |
| [FR-013](inception.md) | P1 | Project Inception |
| [FR-014](inception.md) | P0 | Project Inception |
| [FR-015](inception.md) | P1 | Project Inception |
| [FR-016](inception.md) | P2 | Project Inception |
| [FR-017](development-and-git.md) | P2 | Development and Git |
| [FR-018](development-and-git.md) | P0 | Development and Git |
| [FR-019](development-and-git.md) | P0 | Development and Git |
| [FR-020](workspace.md) | P0 | AI workspace |
| [FR-021](development-and-git.md) | P0 | Development and Git |
| [FR-022](development-and-git.md) | P0 | Development and Git |
| [FR-023](traceability.md) | P0 | Traceability and orchestration |
| [FR-024](development-and-git.md) | P1 | Development and Git |
| [FR-025](development-and-git.md) | P2 | Development and Git |
| [FR-026](ai-and-llm.md) | P0 | AI and LLM |
| [FR-027](ai-and-llm.md) | P0 | AI and LLM |
| [FR-028](ai-and-llm.md) | P0 | AI and LLM |
| [FR-029](ai-and-llm.md) | P0 | AI and LLM |
| [FR-030](ai-and-llm.md) | P0 | AI and LLM |
| [FR-031](ai-and-llm.md) | P0 | AI and LLM |
| [FR-032](ai-and-llm.md) | P1 | AI and LLM |
| [FR-033](ai-and-llm.md) | P1 | AI and LLM |
| [FR-034](ai-and-llm.md) | P1 | AI and LLM |
| [FR-035](distribution.md) | P2 | Distribution and operations |
| [FR-036](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-037](design-and-documentation.md) | P2 | Design and documentation tools |
| [FR-038](design-and-documentation.md) | P1 | Design and documentation tools |
| [FR-039](design-and-documentation.md) | P2 | Design and documentation tools |
| [FR-040](design-and-documentation.md) | P2 | Design and documentation tools |
| [FR-041](delivery-and-testing.md) | P1 | Delivery and testing |
| [FR-042](adapters.md) | P0 | Adapters and capability registry |
| [FR-043](adapters.md) | P2 | Adapters and capability registry |
| [FR-044](delivery-and-testing.md) | P0 | Delivery and testing |
| [FR-045](delivery-and-testing.md) | P0 | Delivery and testing |
| [FR-046](observability-and-feedback.md) | P0 | Observability and feedback |
| [FR-047](traceability.md) | P0 | Traceability and orchestration |
| [FR-048](distribution.md) | P2 | Distribution and operations |
| [FR-049](identity-and-security.md) | P0 | Identity and security |
| [FR-050](adapters.md) | P0 | Adapters and capability registry |
| [FR-051](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-052](identity-and-security.md) | P0 | Identity and security |
| [FR-053](traceability.md) | P0 | Traceability and orchestration |
| [FR-054](distribution.md) | P2 | Distribution and operations |
| [FR-055](coexistence-and-sync.md) | P1 | Coexistence and sync |
| [FR-056](traceability.md) | P0 | Traceability and orchestration |
| [FR-057](distribution.md) | P2 | Distribution and operations |
| [FR-058](development-and-git.md) | P2 | Development and Git |
| [FR-059](distribution.md) | P2 | Distribution and operations |
| [FR-060](adapters.md) | P2 | Adapters and capability registry |
| [FR-061](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-062](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-063](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-064](workspace.md) | P0 | AI workspace |
| [FR-065](workspace.md) | P0 | AI workspace |
| [FR-066](development-and-git.md) | P1 | Development and Git |
| [FR-067](development-and-git.md) | P1 | Development and Git |
| [FR-068](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-069](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-070](requirements-and-planning.md) | P0 | Requirements and planning |
| [FR-071](workspace.md) | P0 | AI workspace |
| [FR-072](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-073](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-074](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-075](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-076](coexistence-and-sync.md) | P1 | Coexistence and sync |
| [FR-077](workspace.md) | P2 | AI workspace |
| [FR-078](workspace.md) | P2 | AI workspace |
| [FR-079](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-080](coexistence-and-sync.md) | P1 | Coexistence and sync |
| [FR-081](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-082](traceability.md) | P1 | Traceability and orchestration |
| [FR-083](traceability.md) | P1 | Traceability and orchestration |
| [FR-084](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-085](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-086](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-087](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-088](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-089](traceability.md) | P2 | Traceability and orchestration |
| [FR-090](coexistence-and-sync.md) | P2 | Coexistence and sync |
| [FR-091](coexistence-and-sync.md) | P1 | Coexistence and sync |
| [FR-092](coexistence-and-sync.md) | P0 | Coexistence and sync |
| [FR-093](workspace.md) | P0 | AI workspace |
| [FR-094](workspace.md) | P0 | AI workspace |
| [FR-095](traceability.md) | P1 | Traceability and orchestration |
| [FR-096](traceability.md) | P1 | Traceability and orchestration |
| [FR-097](delivery-and-testing.md) | P0 | Delivery and testing |
| [FR-098](delivery-and-testing.md) | P1 | Delivery and testing |
| [FR-099](traceability.md) | P0 | Traceability and orchestration |
| [FR-100](traceability.md) | P0 | Traceability and orchestration |
| [FR-101](traceability.md) | P1 | Traceability and orchestration |
| [FR-102](inception.md) | P0 | Project Inception |
| [FR-103](inception.md) | P0 | Project Inception |
| [FR-104](inception.md) | P0 | Project Inception |
| [FR-105](inception.md) | P0 | Project Inception |
| [FR-106](inception.md) | P0 | Project Inception |
| [FR-107](inception.md) | P0 | Project Inception |
| [FR-108](inception.md) | P1 | Project Inception |
| [FR-109](adapters.md) | P0 | Adapters and capability registry |
| [FR-110](ai-and-llm.md) | P0 | AI and LLM |
| [FR-111](ai-and-llm.md) | P0 | AI and LLM |
| [FR-112](adapters.md) | P1 | Adapters and capability registry |
| [FR-113](observability-and-feedback.md) | P0 | Observability and feedback |
| [FR-114](observability-and-feedback.md) | P0 | Observability and feedback |
| [FR-115](observability-and-feedback.md) | P0 | Observability and feedback |
| [FR-116](development-and-git.md) | P0 | Development and Git |
| [FR-117](delivery-and-testing.md) | P0 | Delivery and testing |
| [FR-118](delivery-and-testing.md) | P1 | Delivery and testing |
| [FR-119](delivery-and-testing.md) | P1 | Delivery and testing |
| [FR-120](delivery-and-testing.md) | P0 | Delivery and testing |
| [FR-121](delivery-and-testing.md) | P1 | Delivery and testing |
| [FR-122](observability-and-feedback.md) | P1 | Observability and feedback |
| [FR-123](observability-and-feedback.md) | P1 | Observability and feedback |
| [FR-124](observability-and-feedback.md) | P1 | Observability and feedback |
| [FR-125](identity-and-security.md) | P0 | Identity and security |
| [FR-126](meeting-assistant.md) | P2 | AI meeting assistant |
| [FR-127](adapters.md) | P2 | Adapters and capability registry |
| [FR-128](observability-and-feedback.md) | P1 | Observability and feedback |
| [FR-129](delivery-and-testing.md) | P1 | Delivery and testing |
| [NFR-001](non-functional.md) | — | Non-functional requirements |
| [NFR-002](non-functional.md) | — | Non-functional requirements |
| [NFR-003](non-functional.md) | — | Non-functional requirements |
| [NFR-004](non-functional.md) | — | Non-functional requirements |
| [NFR-005](non-functional.md) | — | Non-functional requirements |
| [NFR-006](non-functional.md) | — | Non-functional requirements |
| [NFR-007](non-functional.md) | — | Non-functional requirements |
| [NFR-008](non-functional.md) | — | Non-functional requirements |
| [NFR-009](non-functional.md) | — | Non-functional requirements |
| [NFR-010](non-functional.md) | — | Non-functional requirements |
| [NFR-011](non-functional.md) | — | Non-functional requirements |
| [NFR-012](non-functional.md) | — | Non-functional requirements |
| [NFR-013](non-functional.md) | — | Non-functional requirements |
| [NFR-014](non-functional.md) | — | Non-functional requirements |
| [NFR-015](non-functional.md) | — | Non-functional requirements |
| [NFR-016](non-functional.md) | — | Non-functional requirements |
| [NFR-017](non-functional.md) | — | Non-functional requirements |
