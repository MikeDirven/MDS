group = "nl.md-systems"
version = "0.0.0.1"

fun MavenArtifactRepository.githubCredentials() {
    credentials {
        username = System.getenv("GITHUB_USER")
        password = System.getenv("GITHUB_KEY") ?: System.getenv("GITHUB_PASS")
    }
}

val privateRepos = listOf(
    "GitHub-MDS" to "https://maven.pkg.github.com/MikeDirven/MDS"
)

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlinjvm) apply false
    alias(libs.plugins.kotlinmultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
}

allprojects {
    apply(plugin = "maven-publish")
    repositories {
        mavenCentral()
        mavenLocal()
    }
    publishing {
        repositories {
            privateRepos.forEach { repo ->
                maven {
                    name = repo.first
                    url = uri(repo.second)
                    githubCredentials()
                }
            }
        }
    }
}