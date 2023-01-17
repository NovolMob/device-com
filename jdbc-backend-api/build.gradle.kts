val serialization_version: String by project
val exposed_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project
val koin_version: String by project

plugins {
    java
    `java-library`
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.bd-practice"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":backend-api"))
                implementation(project(":jdbc-database"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.arrow-kt:arrow-core:1.0.1")
                implementation("io.arrow-kt:arrow-fx-coroutines:1.0.1")

                implementation("io.insert-koin:koin-ktor:$koin_version")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}