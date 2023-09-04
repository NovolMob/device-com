val serialization_version: String by project
val kotlin_version: String by project

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

group = "ru.novolmob.device-com"
version = "0.0.5"

android {
    namespace = "ru.novolmob.core"
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

repositories {
    google()
    mavenCentral()
}

kotlin {
    android()
    jvm()
    js(IR) {
        useCommonJs()
        nodejs()
    }
    wasm {
        browser()
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