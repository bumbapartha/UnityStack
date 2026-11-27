# AI and LLM

[← Documentation index](../README.md)

How UnityStack uses AI models without depending on any one of them.

**Tools:** [AI models](../tools-and-adapters.md#ai-models)

**Decisions:** [PD-2](../decisions/PD-2-adapters-everywhere.md), [AD-1](../decisions/AD-1-java-portable-ai-module.md)

## LLM adapter and model-independent context

- The LLM is reached only through a provider-neutral interface (today: `InceptionAgent` with mock and OpenAI implementations). Provider and model are chosen by configuration.
- A **mock provider** is always available for offline development and automated tests.
- All context an agent needs — conversation, charter, drafts, approved artifacts, decisions, trace links — is stored in UnityStack, Git or the connected tools. Nothing depends on provider-side memory, threads or assistants.
- Switching provider or model takes effect for the next agent turn. The new model is given context rebuilt from those stores, so in-progress work continues.
- Every agent run records the provider and model used (`agentRef`, for example `openai:gpt-4.1-mini`) for audit.
- **Portable AI module ([AD-1](../decisions/AD-1-java-portable-ai-module.md)):** agents, prompts and LLM adapters live in a separate module of the Java code base. The module exposes interfaces with plain data objects (no framework or persistence types), contains no Spring-specific code, and is called by the rest of the application only through those interfaces. This keeps a later move to a separate (for example Python) AI service a contained change ([NFR-016](non-functional.md)).

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-026 | P0 | Provide an LLM adapter that switches model provider and model by configuration, with no workflow changes. |
| FR-110 | P0 | **New.** Always provide a mock LLM provider for offline development and automated tests. |
| FR-111 | P0 | **New.** Keep all agent context in UnityStack, Git or connected tools; after a provider or model switch, the next agent turn receives context rebuilt from those stores, with no dependency on provider-side memory. |
| FR-027 | P0 | Provide an AI business-analyst workflow that turns conversation into structured requirements-as-code. |
| FR-028 | P0 | Separate AI reasoning and requested actions from trusted identity, authorisation, validation, approval and tool execution. |
| FR-029 | P0 | Enforce configured reviewer approval for merges (review may be optional where permitted), mandatory policy and quality gates, and separately configured production deployment authorisation. User finalisation of an AI proposal is always explicit. |
| FR-030 | P0 | Record agent execution state, provider and model, failures, retries and tool-call audit events. |
| FR-031 | P0 | Add role agents progressively: requirements, planning, developer (code and tests), and observe/analyse agents are needed for the MVP loop; UX, architecture and operations agents follow. *(Was P1.)* |
| FR-032 | P1 | Introduce MCP-based tool integration where it adds value, with controlled permissions and validation. |
| FR-033 | P1 | Add RAG over authorised project documents, agent evaluation, and model cost and latency tracing. |
| FR-034 | P1 | Enforce retry limits, budgets, timeouts and human escalation for failed or ambiguous agent actions. |
