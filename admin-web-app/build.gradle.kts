plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}
kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
//                cssSupport {
//                    enabled.set(true)
//                    mode.set("import")
//                }
//                scssSupport {
//                    enabled.set(true)
//                    mode.set("import")
//                }
            }
        }
        binaries.executable()
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":backend-api"))
    implementation(project(":ktor-resources"))

    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.515"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-ring-ui")
//    implementation("org.jetbrains.kotlin-wrappers:kotlin-mui")

    val ktor_version = "2.2.4"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-js:$ktor_version")
    implementation("io.ktor:ktor-client-resources:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.5.0")

    implementation("io.insert-koin:koin-core:3.3.3")

    // for kotlin-ring-ui
    implementation(npm("core-js", "^3.16.0"))
}