val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
    application
    java
    `java-library`
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
}

group = "ru.novolmob"
version = "0.0.1"
application {
    mainClass.set("MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":exposed-database"))
    implementation(project(":backend-api"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")

    testImplementation(kotlin("test"))
}