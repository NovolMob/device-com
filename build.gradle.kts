buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.android.library") version "7.3.1" apply false
    id("com.android.application") version "7.3.1" apply false
    kotlin("multiplatform") version "1.7.20" apply false
    kotlin("android") version "1.6.10" apply false
    id("org.jetbrains.compose") version "1.2.2" apply false
    kotlin("plugin.serialization") version "1.7.20" apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
    }
}