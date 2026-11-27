# Security

[← Requirements](README.md)

Cross-cutting: identity, permissions, security scanning, agent safety and secrets.

**Tools:** [Identity and access](../tools-and-adapters.md#identity-and-access), [Security scanning](../tools-and-adapters.md#security-scanning)

**Decisions:** [AD-3](../decisions.md#ad-3), [TD-02](../decisions.md#td-02)

## Security, identity and governance

1. AI output is attributable to an agent run (provider and model) and a source request.
2. Agents may draft but may not merge protected PRs, obtain unrestricted cloud credentials or deploy to production without the applicable policy and approval checks.
3. **Trusted action boundary:** the LLM proposes an action; non-LLM code checks policy, identity, target, schema, budget and approval before executing it.
4. **Identity model:** distinguish the human requester or approver, the logical AI agent, and the trusted execution service; record all three.
5. **Least privilege:** GitHub App with scoped permissions, short-lived credentials where available, separate read / propose / execute privileges.
6. **Untrusted input:** repository files, retrieved documents, tool output, issue text and **logs** are untrusted; instructions inside them never override system policy.
7. **Secrets** never go into Git or model prompts unnecessarily. Real keys live only in `.env` (git-ignored) locally, or a secrets manager when deployed; `.env.example` holds placeholders only.
8. **Sign-in from the start:** UnityStack uses Keycloak (OpenID Connect) even locally; any environment reachable from the internet must also use HTTPS. Keycloak realm configuration is exported to a file in Git (no secrets) so the local setup rebuilds the same way.
9. Sandboxed code execution, secret redaction, outbound access controls and emergency revocation are required as the developer agent matures.
10. Every adapter defines its authority, sync direction, conflict handling and audit events.

**Acceptance:** an AI agent cannot merge a protected PR, obtain an unrestricted cloud credential or deploy to production without the applicable policy and approval checks.

## Data, provenance and safety rules

- Store organisation/project/repo/artifact identity and exact baseline commit SHA for every draft.
- If upstream baseline changes, show conflict/rebase options; never silently overwrite.
- Treat repository content, tool responses and retrieved documents as untrusted inputs to agents; prevent prompt injection from becoming executable authority.
- Use least-privilege GitHub App permissions and short-lived credentials; secret storage in AWS Secrets Manager/SSM.
- Separate human actor, agent run and trusted execution service identities in audit records.
- Validate AI-generated patches against schemas, path constraints and repository policies before writing.
- Make sync jobs idempotent; use source-specific stable IDs, event deduplication and checkpoints.
- Source tools remain authoritative for their operational records; UnityStack maintains derived records and relationships.
- Retain source evidence for trace links; mark inferred links with confidence and verification status.
- Do not promise perfect historical reconstruction: deleted, inaccessible or never-recorded source data may be unrecoverable.
- Protect production with branch/environment rules, tests, security gates and optional release approvals.

## Identity and access requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-052 | P0 | All agent action requests pass through a trusted policy, authorisation and execution layer with scoped credentials and approval checks. |
| FR-049 | P0 | Provide central identity, session management and platform roles through the identity provider (Keycloak, [AD-3](../decisions.md#ad-3)); map platform identities to tool access where supported and document exceptions. |
| FR-125 | P0 | **New.** Enforce resource-level permissions: which user or role may perform which operation (for example view, edit, finalise, approve, deploy) on which resource (for example a project, artifact, PR or environment). The identity provider supplies identity, groups and broad roles; UnityStack evaluates project-level rules. |

## Security scanning requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-117 | P0 | **New.** Run static code security scanning (SAST) and dependency, image, infrastructure and secret scanning (SCA) in CI on every PR; link findings to the PR and Change ID; failing scans block merge according to policy. |
| FR-118 | P1 | **New.** Run dynamic black-box security testing (DAST) against the deployed application after deployment; link findings to the deployment and Change ID, and let AI propose bugs from them ([FR-115](06-monitoring-and-operations.md)). |
