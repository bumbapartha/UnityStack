# Backend guidelines

Read the root `CLAUDE.md` first. This file covers the Spring Boot control plane.

## Stack

Java 21, Spring Boot 4.1 (Spring Framework 7), Gradle 9 (Kotlin DSL, via the wrapper), PostgreSQL, Flyway migrations, Spring Data JPA (Hibernate 7) for data access (TD-12), Spring Modulith 2.1, JUnit with AssertJ. JSON is **Jackson 3**: import from `tools.jackson.*` (annotations stay in `com.fasterxml.jackson.annotation`). Take the versions Spring Boot manages; do not override single libraries.

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
    persistence/   shared JPA support (JSONB mapping, AssignedIdEntity)
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
4. **Own your data.** Tables for a new module are prefixed with the module name (for example `testing_run`). No module reads another module's tables. The original inception tables (`project`, `charter_revision`, …) are unprefixed; renaming them is a separate change.
5. **Boundary test.** `ModularityTest` runs `ApplicationModules.of(UnityStackApplication.class).verify()` with Spring Modulith; it must pass. Other modules may use only `core.ai`, `core.common` and `core.persistence` from core, and only `api` from a feature module (marked with `@NamedInterface`). Do not weaken or skip it to make a change compile.

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

- **Spring Data JPA (TD-12).** Entities and Spring Data repositories live in the module's `persistence/` package and are internal to the module. Never expose an entity outside its module. Map entities to records or DTOs in `api/`.
- **Flyway owns the schema.** Every schema change is a Flyway migration. Set `spring.jpa.hibernate.ddl-auto=validate`, so Hibernate never creates or changes tables itself.
- **Stable data in columns, flexible content in JSONB.** Projects, Change IDs, statuses, actors, timestamps and links go in normal columns. Artifact content that changes shape (charters, requirement drafts, AI proposals) goes in a PostgreSQL `jsonb` column, mapped with `@JdbcTypeCode(SqlTypes.JSON)` to a record or `Map`. Adding a field to that content needs no migration.
- **Revisions are new rows.** Never update an artifact revision in place; insert a new revision row that points to its parent. This keeps history and audit intact.
- **No relationships across modules.** Reference another module's data by ID only, never through `@ManyToOne` or `@OneToMany` to its entities. Within a module, prefer `LAZY` fetching and explicit fetch joins or projections for reads, and watch for N+1 queries.
- **Transactions** belong on application services (`@Transactional`), not controllers or repositories.
- For complex reads, use JPQL or Spring Data projections; use native SQL only where JSONB operators are needed, always with bound parameters. Never concatenate SQL strings with user input.
- Do not use `JdbcClient` or `JdbcTemplate` alongside JPA.
- Entities with application-assigned UUIDs extend `core.persistence.AssignedIdEntity`, so `save()` always inserts. Mark revision and audit entities `@Immutable`.
- JSONB is read and written with the application's Jackson 3 `JsonMapper` (`core.persistence.JpaJsonConfiguration`; unknown fields are ignored), so older rows with extra fields still load.
- Migrations go in `src/main/resources/db/migration`, named `V<n>__<module>_<description>.sql`, with versions numbered globally. Never edit a migration that has already been merged; add a new one.

## Readability and formatting

- **Small methods.** One method does one thing. If it needs a comment to separate its steps, split it into well-named methods.
- **Clear names.** Name classes, methods and variables after what they mean in the domain (`latestRevision`, not `r` or `tmp`). Short names are fine only for tiny lambdas and loop indexes.
- **Comments explain why.** The code says what it does; a comment says why it does it that way, what constraint it follows or which requirement it implements (`// FR-105: nothing is stored until confirmation`). Delete comments that only repeat the code.
- **No clever one-liners.** Prefer a few plain statements over a dense stream chain, nested ternary or packed expression. Readers come first.
- **Formatting is automatic.** Spotless with Palantir Java Format (4 spaces, 120 columns) owns the layout; do not format by hand. **Always run `./gradlew spotlessApply` before committing.** `./gradlew build` and CI fail on unformatted code (`spotlessCheck`). Without a local JDK, run it in Docker (see `docs/dev-setup.md`).

## Dependencies

- `gradle.lockfile` pins the shipped (`runtimeClasspath`) dependencies so Trivy can scan them (TD-02). After adding, removing or upgrading a dependency, run `./gradlew dependencies --configuration runtimeClasspath --write-locks` and commit the lockfile; the build fails if it is out of date.

## Errors and API

- Throw `ApiException` with an HTTP status for expected errors; the shared handler turns it into JSON `{ "error": ... }`.
- REST paths are `/api/<module>/...`. Request and response bodies are records in the module's `api/` package.

## Tests

- Unit-test services with mock adapters and the mock LLM provider (as `InceptionFlowTest` does). No network calls in unit tests.
- Name tests after the behaviour, and reference the requirement ID in a comment where it helps (for example `// FR-105`).
- Run `./gradlew test` before opening a PR.
