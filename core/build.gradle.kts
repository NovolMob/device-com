val serialization_version: String by project
val kotlin_version: String by project

plugins {
    java
    `java-library`
    kotlin("multiplatform")
    id("maven-publish")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.device-com"
version = "0.0.5"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js(IR) {
        useCommonJs()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("uuid", "9.0.0"))
                implementation(npm("js-big-decimal", "1.3.15"))
            }
        }
        val jsTest by getting {
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