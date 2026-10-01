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
    implementation(libs.spring.boot.starter.test)
    developmentOnly(libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.configuration.processor)
    compileOnly(libs.projectlombok)
    annotationProcessor(libs.projectlombok)
    testImplementation(platform(libs.junit))
    testCompileOnly(libs.projectlombok)
    testAnnotationProcessor(libs.projectlombok)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
