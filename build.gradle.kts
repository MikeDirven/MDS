plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "nl.md-systems"
version = "0.0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(15)
}

application {
    mainClass.set("MainKt")
}