plugins {
    kotlin("multiplatform") version "1.9.0"
    id("maven-publish")
}

val user: String = System.getenv("GITHUB_USER")
val key: String = System.getenv("GITHUB_KEY")
group = "nl.md-systems"
version = "0.0.0.2"

repositories {
    mavenCentral()
}

publishing{
    repositories {
        maven {
            name = "GitHub"
            url = uri("https://maven.pkg.github.com/MikeDirven/MDS") // Github Package
            credentials {
                //Fetch these details from the properties file or from Environment variables
                username = user
                password = key
            }
        }
    }
}

kotlin {
    jvm() {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
//            dependencies {
//                implementation(project(mapOf("path" to ":mds_engine")))
//            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.8.1")
            }
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}