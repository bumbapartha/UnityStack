# AD-1 — Java only, with a portable AI module

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026 · **Former open question:** D-1

## Context

The docs planned a separate Python/LangGraph AI service; the built code runs AI in Spring Boot. Java is the founder's core language.

## Decision

Keep AI in Java for now. Agents, prompts and LLM adapters live in their own module behind clean interfaces with plain data objects and no Spring-specific code, so the module can later be moved to a Python service with AI help.

## Consequences

One language and one service to run. Newer AI frameworks may appear in Python first; the module boundary keeps a later move contained ([NFR-016](../requirements/non-functional.md)).

## Related

[AI and LLM](../requirements/ai-and-llm.md), [Architecture](../architecture.md), [Non-functional requirements](../requirements/non-functional.md)
