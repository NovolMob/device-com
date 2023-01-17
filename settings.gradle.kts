pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "bd-practice"

//include("admin-web-app")
include("backend")
include("backend-api")
include("core")
include("exposed-backend-api")
include("exposed-database")
include("jdbc-backend-api")
include("jdbc-database")
include("ktor-resources")
