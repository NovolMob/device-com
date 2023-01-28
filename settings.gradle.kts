pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
