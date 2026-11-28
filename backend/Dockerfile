FROM gradle:8.10.2-jdk21 AS build
WORKDIR /src
COPY settings.gradle.kts build.gradle.kts gradle.lockfile ./
RUN gradle dependencies --no-daemon -q > /dev/null
COPY src ./src
RUN gradle test bootJar --no-daemon

FROM eclipse-temurin:21-jre
# Run as a dedicated non-root user (Trivy DS-0002, Semgrep missing-user-entrypoint).
RUN groupadd --system --gid 10001 unitystack \
    && useradd --system --uid 10001 --gid unitystack --no-create-home unitystack
WORKDIR /app
COPY --from=build /src/build/libs/unitystack-backend-*.jar app.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
