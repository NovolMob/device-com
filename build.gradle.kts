buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    kotlin("multiplatform") version "1.8.10" apply false
    kotlin("jvm") version "1.8.10" apply false
    kotlin("android") version "1.8.10" apply false

    id("com.android.library") version "7.3.0" apply false
    id("com.android.application") version "7.3.0" apply false

    id("org.jetbrains.compose") version "1.4.0-dev-wasm06" apply false

    kotlin("plugin.serialization") version "1.8.10" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }

    configurations.all {
        val conf = this
        // Currently it's necessary to make the android build work properly
        conf.resolutionStrategy.eachDependency {
            val isWasm = conf.name.contains("wasm", true)
            val isJs = conf.name.contains("js", true)
            val isComposeGroup = requested.module.group.startsWith("org.jetbrains.compose")
            val isComposeCompiler = requested.module.group.startsWith("org.jetbrains.compose.compiler")
            if (isComposeGroup && !isComposeCompiler && !isWasm && !isJs) {
                useVersion("1.4.0")
            }
        }
    }
}