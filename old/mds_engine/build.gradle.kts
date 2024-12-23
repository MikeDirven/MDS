plugins {
    kotlin("multiplatform")
    id("maven-publish")
    application
}

val user: String = System.getenv("GITHUB_USER")
val key: String = System.getenv("GITHUB_KEY")
group = "nl.md-systems"
version = "0.0.0.1"

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

//dependencies {
//    testImplementation(kotlin("test"))
//    implementation("com.google.code.gson:gson:2.8.9")
//}

tasks.test {
    useJUnitPlatform()
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
            dependencies {
                api("ch.qos.logback:logback-classic:1.4.11")
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1")
            }
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