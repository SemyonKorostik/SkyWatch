plugins {
//    java
    kotlin("jvm") version "2.4.0"
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.korostik"
version = "0.0.1-SNAPSHOT"
description = "SkyWatch"

/*java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}*/

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.ksilisk:telegram-bot-spring-boot-starter:0.7.0")
    runtimeOnly("org.postgresql:postgresql:42.7.11")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:5.0.2")
    implementation("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok:1.18.46")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.46")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
