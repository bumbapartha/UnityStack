# Deployment phase

[← Requirements](README.md)

Builds, environments, images and releases.

**Tools:** [CI/CD](../tools-and-adapters.md#cicd), [Container registry](../tools-and-adapters.md#container-registry), [Feature flags](../tools-and-adapters.md#feature-flags)

**Decisions:** [TD-01](../decisions.md#td-01), [TD-04](../decisions.md#td-04), [TD-08](../decisions.md#td-08), [TD-10](../decisions.md#td-10)

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-097 | P0 | Run a Hello World walking skeleton that is rebuilt and redeployed automatically: locally by Docker Compose during the thin loop, and through a CI/CD pipeline to AWS environments after the move. |
| FR-129 | P1 | **New.** After the AWS move, deploy each PR to its own preview environment for testing, and deploy merges to main to the main (production) environment, with environment protection. |
| FR-044 | P0 | Version tests, CI/CD pipelines, deployment configuration and monitoring configuration in Git. *(Was P1.)* |
| FR-045 | P0 | Run validation and deployment through trusted pipelines with environment protection. *(Was P1.)* |
| FR-119 | P1 | **New.** Publish built images to the artifact registry through its adapter, tagged with version, commit SHA and Change ID. Needed from the AWS move; local development builds images locally. |
| FR-121 | P1 | **New.** Control feature exposure through feature flags reached via the OpenFeature SDK, with per-environment settings; flag changes are audited. |
| FR-098 | P1 | Support configured automatic delivery or human production release approval with quality gates. |
