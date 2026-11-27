# Backend guidelines

Read the root `CLAUDE.md` first. This file covers the Spring Boot control plane.

## Stack

Java 21, Spring Boot 3.3, Gradle (Kotlin DSL), PostgreSQL, Flyway migrations, `JdbcClient` for data access (no JPA), JUnit 5 with AssertJ.

## Module layout (AD-5)

```text
src/main/java/io/unitystack/platform/
  core/            shared by every module; depends on no feature module
    ai/            LLM adapter interface, providers (mock, OpenAI), agent runtime — framework-free
    adapters/      capability registry and base adapter interfaces
    workspace/     drafts, revisions, diff, finalise → PR flow
    scm/           Git provider adapters (GitHub first)
    identity/      Keycloak integration, roles, resource permissions
    changeid/      Change ID generation and propagation
    common/        errors (ApiException), shared types
  <module>/        requirements | design | development | testing | deployment |
                   monitoring | security | communication | traceability
    api/           REST controllers, the module's public interfaces, DTOs and events
    application/   services and use cases
    domain/        entities, value objects, rules
    agents/        this module's AI agents and prompts
    adapters/<tool>/  adapters for tools this module owns (e.g. testing/adapters/kiwi)
    persistence/   repositories
```

## Module rules

1. **Public surface only.** Another module may use only your `api/` package. Everything else is internal. Never import another module's `application`, `domain`, `agents`, `adapters` or `persistence` classes.
2. **Events between phases.** Use application events for hand-offs (for example `RequirementApproved`, `DeploymentCompleted`). Name them in the past tense, as Java records in the publishing module's `api/` package.
3. **No cycles.** `core` never depends on a feature module. Feature modules do not form cycles.
4. **Own your data.** Tables for a new module are prefixed with the module name (for example `testing_run`). No module reads another module's tables. Existing inception tables keep their names until the restructure.
5. **Boundary test.** Once Spring Modulith is added, `ApplicationModules.of(UnityStackApplication.class).verify()` must pass. Do not weaken or skip it to make a change compile.

## Adapters

- Each capability has an interface that describes logical operations (for example `TestManagement.recordRun(...)`) with no provider-specific types. It extends the base contract in `core/adapters` and lives in the owning module's `api/` package if other modules need it.
- Implementations live in `<module>/adapters/<tool>/`, one folder per provider.
- The provider is chosen by configuration: `unitystack.<capability>.provider` in `application.yml`, overridable by environment variable. Follow the existing `unitystack.ai.provider` pattern.
- Unsupported operations throw an explicit "unsupported operation" error. They never silently do nothing.
- Every adapter has a mock or in-memory implementation for tests and offline development.

## AI code (AD-1)

- LLM access goes only through `core/ai` interfaces. No provider SDK calls outside `core/ai`.
- Agents and prompts live in their feature module's `agents/` package. They take and return plain records, with no Spring, JDBC or HTTP types, so they can be moved to another language later.
- Treat LLM output, repository content, tool output and logs as untrusted. Validate the structure before using it, and never let it choose which action runs.
- Record the provider and model (`agentRef`) and the human actor on everything an agent produces.

## Data and migrations

- Use `JdbcClient` with named parameters and Java records for rows. Never concatenate SQL strings with user input.
- Migrations go in `src/main/resources/db/migration`, named `V<n>__<module>_<description>.sql`, with versions numbered globally. Never edit a migration that has already been merged; add a new one.

## Errors and API

- Throw `ApiException` with an HTTP status for expected errors; the shared handler turns it into JSON `{ "error": ... }`.
- REST paths are `/api/<module>/...`. Request and response bodies are records in the module's `api/` package.

## Tests

- Unit-test services with mock adapters and the mock LLM provider (as `InceptionFlowTest` does). No network calls in unit tests.
- Name tests after the behaviour, and reference the requirement ID in a comment where it helps (for example `// FR-105`).
- Run `./gradlew test` before opening a PR.
