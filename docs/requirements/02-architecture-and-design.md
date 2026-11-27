# Architecture and design phase

[← Requirements](README.md)

How UnityStack is designed: adapters, the AI module and the design and documentation tools.

**Tools:** [AI models](../tools-and-adapters.md#ai-models), [Design, documentation and developer tools](../tools-and-adapters.md#design-documentation-and-developer-tools)

**Decisions:** [PD-2](../decisions.md#pd-2), [AD-1](../decisions.md#ad-1)

## Adapters and capability registry

- Workflows and agents call **logical operations** on a capability, for example `projectManagement.createStory`, `scm.openPullRequest`, `ci.getRunStatus`, `observability.queryLogs`, `llm.complete`.
- An **adapter** translates those operations to one provider's API. Provisioning a tool and connecting to an existing instance are separate adapter concerns.
- A **capability registry** records, for each capability in each installation: provider ID, connection mode (provisioned or existing), endpoint, credential reference, supported operations, health and scope.
- Unsupported or edition-restricted operations return an explicit "unsupported operation" error; callers must handle it.
- MVP needs one working provider per capability used in the loop. The interfaces must not leak provider-specific concepts, so that a second provider can be added without changing workflows.
- **Swap rule:** changing the provider of a capability means adding or selecting an adapter and changing configuration — nothing else in UnityStack changes ([NFR-015](11-non-functional.md)).
- **Selection:** today the provider is chosen in configuration. Later, an organisation chooses the provider for each capability at install time, and the installer deploys or connects only those (FR-043, FR-060).
- Where a provider already has a standard client abstraction, the adapter may use it (for example OpenFeature for feature flags, OpenID Connect for identity). Where an MCP server exists for a tool, an adapter may call it, subject to the trusted execution rules ([Security](07-security.md)); community MCP servers are checked for maturity before use.

### Adapter requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-109 | P0 | **New.** Every external tool is accessed only through a capability adapter; workflows and agents contain no provider-specific API calls. |
| FR-042 | P0 | Provide a capability registry and stable provider-independent interfaces for every capability used in the MVP loop. |
| FR-050 | P0 | The registry maps logical operations to installed or connected provider adapters, with endpoint, credential reference, supported operations and health. |
| FR-005 | P0 | Show each configured tool in the UnityStack portal with endpoint, connection mode and health. *(Was: show all nine installed tools.)* |
| FR-127 | P2 | **New.** Support further Git providers behind the source-control adapter (Gitea, GitLab, Bitbucket, Azure DevOps Repos), mapping branches, commits, pull/merge requests, reviews, branch protection and webhooks; operations a provider lacks return an explicit unsupported-operation error. |
| FR-112 | P1 | **New.** Add Jira as a second project-management provider behind the same adapter, proving that the provider can be swapped without workflow changes. |
| FR-043 | P2 | Allow each capability to use a UnityStack-provisioned default, an existing customer instance, or a supported alternative provider, chosen by the customer. |
| FR-060 | P2 | Allow users to select an alternative provider, connect an existing enterprise instance, or provision a supported alternative per capability (BYOT) through the UI. |

## AI and LLM

- The LLM is reached only through a provider-neutral interface (today: `InceptionAgent` with mock and OpenAI implementations). Provider and model are chosen by configuration.
- A **mock provider** is always available for offline development and automated tests.
- All context an agent needs — conversation, charter, drafts, approved artifacts, decisions, trace links — is stored in UnityStack, Git or the connected tools. Nothing depends on provider-side memory, threads or assistants.
- Switching provider or model takes effect for the next agent turn. The new model is given context rebuilt from those stores, so in-progress work continues.
- Every agent run records the provider and model used (`agentRef`, for example `openai:gpt-4.1-mini`) for audit.
- **Portable AI module ([AD-1](../decisions.md#ad-1)):** agents, prompts and LLM adapters live in a separate module of the Java code base. The module exposes interfaces with plain data objects (no framework or persistence types), contains no Spring-specific code, and is called by the rest of the application only through those interfaces. This keeps a later move to a separate (for example Python) AI service a contained change ([NFR-016](11-non-functional.md)).

### AI requirements

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

## Design and documentation tools

| ID | Priority | Requirement |
|---|---|---|
| FR-038 | P1 | Generate Mermaid diagrams and ADRs through human-reviewed PRs. |
| FR-039 | P2 | Publish Markdown documentation through MkDocs and OpenAPI specifications through Swagger UI. *(Was P1.)* |
| FR-037 | P2 | Generate and review Excalidraw wireframes and Penpot designs with Git-backed artifacts. *(Was P1.)* |
| FR-040 | P2 | Register software components in Backstage using Git-versioned catalogue descriptors. *(Was P1.)* |
