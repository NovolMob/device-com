val koin_version: String by project
val ktor_version: String by project

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        moduleName = "devicecom"
        browser()
        binaries.executable()
    }

    wasm {
        moduleName = "devicecom"
        browser {
            commonWebpackConfig {
                devServer?.port = 3000
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsWasmMain by creating {
            dependencies {
                implementation(project(":core"))
//                implementation(project(":backend-api"))
//                implementation(project(":ktor-resources"))
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val wasmMain by getting {
            dependsOn(jsWasmMain)
        }
        val jsMain by getting {
            dependsOn(jsWasmMain)
        }
    }
}

compose.experimental {
    web.application {}
}