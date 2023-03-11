val exposed_version: String by project
val postgresql_version: String by project
val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    java
    `java-library`
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.device-com"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
                implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
                implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
                implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
                implementation("org.jetbrains.exposed:exposed-crypt:$exposed_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.postgresql:postgresql:$postgresql_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}