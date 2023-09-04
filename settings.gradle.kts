pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

rootProject.name = "device-com"

//include("admin-web-app")
//include("user-mobile-app")
include("backend")
include("backend-api")
include("core")
//include("exposed-backend-api")
//include("exposed-database")
include("jdbc-backend-api")
include("jdbc-database")
include("ktor-resources")
