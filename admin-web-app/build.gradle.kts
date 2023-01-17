import org.jetbrains.compose.jetbrainsCompose

val koin_version: String by project
val ktor_version: String by project

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
    jetbrainsCompose()
}

kotlin {
    js(IR) {
        browser {
            runTask {
                devServer?.port = 3000
            }
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                    mode = "import"
                }
                scssSupport {
                    enabled = true
                    mode = "import"
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(project(":core"))
                implementation(project(":backend-api"))
                implementation(project(":ktor-resources"))
                implementation(npm("three-dots", "0.3.2"))
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-js:$ktor_version")
                implementation("io.ktor:ktor-client-resources:$ktor_version")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
                compileOnly("io.insert-koin:koin-core:$koin_version")
            }
        }
    }
}