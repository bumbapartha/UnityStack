# PD-2 — Adapters everywhere

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Organisations use many different SDLC tools and AI models, and do not want lock-in.

## Decision

Every SDLC tool and the LLM are reached only through an adapter. The selected open-source tools are the default providers; alternatives such as Jira, or a different model, must be swappable. Bundling the defaults into an installer is a later packaging concern.

## Consequences

Workflows and agents call logical capabilities, never a provider API. UnityStack is the fixed core; every tool around it can change.

## Related

[Adapters and capability registry](../requirements/adapters.md), [Tools and adapters](../tools-and-adapters.md), [AI and LLM](../requirements/ai-and-llm.md)
