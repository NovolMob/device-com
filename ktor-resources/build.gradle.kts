val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
    application
    java
    `java-library`
    id("maven-publish")
    kotlin("multiplatform") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
}

group = "ru.novolmob.bd-practice"
version = "0.0.5"
application {
    mainClass.set("MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":backend-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

                implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-resources:$ktor_version")
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