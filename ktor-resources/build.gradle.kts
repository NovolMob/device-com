val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
    java
    `java-library`
    id("maven-publish")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.bd-practice"
version = "0.0.5"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":backend-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("io.ktor:ktor-client-resources:$ktor_version")
            }
        }
    }
}

publishing {
    publications {
        registerPublication(group.toString(), name, version.toString())
    }
}

fun PublicationContainer.registerPublication(groupId: String, artifactId: String, version: String) =
    register<MavenPublication>(artifactId) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        artifact(tasks["jar"])
    }