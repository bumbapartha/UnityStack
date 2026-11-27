# Delivery and testing

[← Documentation index](../README.md)

Builds, security scans, tests, images, deployments and feature flags.

**Tools:** [CI/CD](../tools-and-adapters.md#cicd), [Container registry](../tools-and-adapters.md#container-registry), [Security scanning](../tools-and-adapters.md#security-scanning), [Testing](../tools-and-adapters.md#testing), [Feature flags](../tools-and-adapters.md#feature-flags), [Design, documentation and developer tools](../tools-and-adapters.md#design-documentation-and-developer-tools)

**Decisions:** [TD-01](../decisions/TD-01-ci-cd-github-actions.md), [TD-02](../decisions/TD-02-security-scanning.md), [TD-03](../decisions/TD-03-testing-tools.md), [TD-04](../decisions/TD-04-container-registry.md), [TD-08](../decisions/TD-08-feature-flags.md), [TD-10](../decisions/TD-10-local-rebuild-workflow.md)

## Requirements

| ID | Priority | Requirement |
|---|---|---|
| FR-097 | P0 | Run a Hello World walking skeleton that is rebuilt and redeployed automatically: locally by Docker Compose during the thin loop, and through a CI/CD pipeline to AWS environments after the move. |
| FR-129 | P1 | **New.** After the AWS move, deploy each PR to its own preview environment for testing, and deploy merges to main to the main (production) environment, with environment protection. |
| FR-044 | P0 | Version tests, CI/CD pipelines, deployment configuration and monitoring configuration in Git. *(Was P1.)* |
| FR-045 | P0 | Run validation and deployment through trusted pipelines with environment protection. *(Was P1.)* |
| FR-117 | P0 | **New.** Run static code security scanning (SAST) and dependency, image, infrastructure and secret scanning (SCA) in CI on every PR; link findings to the PR and Change ID; failing scans block merge according to policy. |
| FR-118 | P1 | **New.** Run dynamic black-box security testing (DAST) against the deployed application after deployment; link findings to the deployment and Change ID, and let AI propose bugs from them ([FR-115](observability-and-feedback.md)). |
| FR-119 | P1 | **New.** Publish built images to the artifact registry through its adapter, tagged with version, commit SHA and Change ID. Needed from the AWS move; local development builds images locally. |
| FR-120 | P0 | **New.** Keep test cases in Git, record every test run's results (unit, end-to-end, manual) in the test-management tool against the build or release, and link them to requirement, story and Change ID; provide a per-release test report kept in history. |
| FR-121 | P1 | **New.** Control feature exposure through feature flags reached via the OpenFeature SDK, with per-environment settings; flag changes are audited. |
| FR-098 | P1 | Support configured automatic delivery or human production release approval with quality gates. |
| FR-041 | P1 | Bring code-quality results (SonarQube) and IDE workflows into PR review. |
