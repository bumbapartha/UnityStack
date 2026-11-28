plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless") version "8.12.0"
}

group = "io.unitystack"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// Spring Modulith 1.2.x is the line for Spring Boot 3.3 (AD-5 boundary checks).
extra["springModulithVersion"] = "1.2.13"

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.modulith:spring-modulith-api")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Code formatting. spotlessCheck is part of `check`, so `./gradlew build` fails on unformatted code;
// run `./gradlew spotlessApply` to fix it. Palantir Java Format: 4-space indent, 120 columns.
spotless {
    java {
        target("src/**/*.java")
        palantirJavaFormat("2.103.0")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Only produce the executable Spring Boot jar.
tasks.named<Jar>("jar") {
    enabled = false
}
