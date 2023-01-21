val serialization_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.device-com"
version = "0.0.8"

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "ru.novolmob.backendapi"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    android()
    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }
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
        val jsMain by getting {
            dependencies {
                implementation(npm("js-sha512", "0.8.0"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}