plugins {
    kotlin("jvm") version "2.0.0"
}

group = "nl.md-systems"
version = "0.0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":engine"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}