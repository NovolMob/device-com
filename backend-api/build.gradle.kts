val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    java
    `java-library`
    kotlin("multiplatform")
    id("maven-publish")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.bd-practice"
version = "0.0.8"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.arrow-kt:arrow-core:1.0.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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