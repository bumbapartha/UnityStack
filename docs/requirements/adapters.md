# Adapters and capability registry

[← Documentation index](../README.md)

How UnityStack reaches every external tool.

**Tools:** [Source control](../tools-and-adapters.md#source-control), [AI models](../tools-and-adapters.md#ai-models), [Project management](../tools-and-adapters.md#project-management)

**Decisions:** [PD-2](../decisions/PD-2-adapters-everywhere.md)

## Capabilities and adapters

- Workflows and agents call **logical operations** on a capability, for example `projectManagement.createStory`, `scm.openPullRequest`, `ci.getRunStatus`, `observability.queryLogs`, `llm.complete`.
- An **adapter** translates those operations to one provider's API. Provisioning a tool and connecting to an existing instance are separate adapter concerns.
- A **capability registry** records, for each capability in each installation: provider ID, connection mode (provisioned or existing), endpoint, credential reference, supported operations, health and scope.
- Unsupported or edition-restricted operations return an explicit "unsupported operation" error; callers must handle it.
- MVP needs one working provider per capability used in the loop. The interfaces must not leak provider-specific concepts, so that a second provider can be added without changing workflows.
- **Swap rule:** changing the provider of a capability means adding or selecting an adapter and changing configuration — nothing else in UnityStack changes ([NFR-015](non-functional.md)).
- **Selection:** today the provider is chosen in configuration. Later, an organisation chooses the provider for each capability at install time, and the installer deploys or connects only those (FR-043, FR-060).
- Where a provider already has a standard client abstraction, the adapter may use it (for example OpenFeature for feature flags, OpenID Connect for identity). Where an MCP server exists for a tool, an adapter may call it, subject to the trusted execution rules ([Identity and security](identity-and-security.md)); community MCP servers are checked for maturity before use.

## Requirements

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
