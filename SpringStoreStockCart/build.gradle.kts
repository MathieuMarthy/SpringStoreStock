import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
