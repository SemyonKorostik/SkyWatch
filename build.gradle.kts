plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
}

group = "dev.korostik"
version = "0.0.1-SNAPSHOT"
description = "SkyWatch"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    implementation(platform(libs.spring.cloud))
    implementation(libs.spring.cloud.openfeign)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.web)
    developmentOnly(libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.configuration.processor)
    compileOnly(libs.projectlombok)
    compileOnly(libs.mapsrtruct)
    annotationProcessor(libs.projectlombok)
    annotationProcessor(libs.mapsrtruct)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.spring.boot.starter.liquibase)
    testImplementation(platform(libs.junit))
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.mapsrtruct)
    testCompileOnly(libs.projectlombok)
    testAnnotationProcessor(libs.projectlombok)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
