val githubMavenPackages = "https://maven.pkg.github.com"

plugins {
    `java`
    id("maven-publish")
}

allprojects {
    if (rootProject == this) return@allprojects
    afterEvaluate {
        plugins.apply("maven-publish")
        publishing {
            repositories {
                mavenGithubRepository("NovolMob/bd-practice")
            }
        }
    }
}

fun runCommand(vararg command: String): String? = ProcessBuilder()
    .command(*command)
    .start()
    .inputStream
    .use { String(it.readAllBytes()).dropLast(1) }
    .ifEmpty { null }

fun getGitUsername(): String? = runCommand("git", "config", "--get", "user.name")
fun getGitPassword(): String? = runCommand("git", "config", "--get", "user.token")

fun RepositoryHandler.mavenGitRepository(path: Any) = maven {
    url = uri(path)
    credentials {
        username = getGitUsername()
        password = getGitPassword()
    }
}

fun RepositoryHandler.mavenGithubRepository(name: String) = mavenGitRepository("$githubMavenPackages/$name")
