val serialization_version: String by project
val exposed_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project
val koin_version: String by project

plugins {
    application
    java
    `java-library`
    kotlin("multiplatform")
    id("io.ktor.plugin") version "2.2.2"
    kotlin("plugin.serialization")
}

group = "ru.novolmob.device-com"
version = "0.0.1"
application {
    mainClass.set("ru.novolmob.backend.MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

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
                implementation(project(":ktor-resources"))
                implementation(project(":exposed-backend-api"))
                implementation(project(":exposed-database"))
                implementation(project(":exposed-database"))
                implementation(project(":jdbc-database"))
                implementation(project(":jdbc-backend-api"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("io.arrow-kt:arrow-core:1.0.1")
                implementation("io.arrow-kt:arrow-fx-coroutines:1.0.1")

                implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
                implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")

                implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-resources:$ktor_version")
                implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
                implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("ch.qos.logback:logback-classic:$logback_version")

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