# Architecture and design phase

[← Requirements](README.md)

Features UnityStack gives its users for the architecture and design phase of _their_ projects: diagrams, ADRs, wireframes, UX design, API and technical documentation, and the software catalogue. UnityStack's own architecture is described in [Architecture](../architecture.md); its platform requirements are in [Core platform](10-core-platform.md).

**Tools:** [Design, documentation and developer tools](../tools-and-adapters.md#design-documentation-and-developer-tools)

## Requirements

| ID     | Priority | Requirement                                                                                                                                                                                                                                                                                                        |
| ------ | -------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| FR-038 | P0       | Generate the HLD (architecture decisions as ADRs, including monolith or microservices, frontend/backend split, stack and how each non-functional requirement is met), the LLD and Mermaid diagrams, including sequence diagrams, through human-reviewed PRs. _(Was P1.)_                                           |
| FR-039 | P0       | Publish the architecture documents (HLD, LLD, ADRs as Markdown with Mermaid diagrams, kept in Git) as a documentation website with MkDocs, giving a shareable URL for reading and review, and publish OpenAPI specifications (API contracts) through Swagger UI. No separate Word or Google documents. _(Was P2.)_ |
| FR-132 | P0       | **New.** Serve each managed project's published docs site, and a preview build for every PR, from S3-compatible storage through UnityStack, readable only by signed-in users with access to that project ([TD-14](../decisions.md#td-14)).                                                                         |
| FR-037 | P0       | Generate and review Excalidraw wireframes and Penpot designs with Git-backed artifacts, after the requirements are approved and before planning. _(Was P2.)_                                                                                                                                                       |
| FR-040 | P2       | Register software components in Backstage using Git-versioned catalogue descriptors. _(Was P1.)_                                                                                                                                                                                                                   |
