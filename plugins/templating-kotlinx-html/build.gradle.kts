plugins {
    kotlin("jvm") version "2.1.0"
}

group = "nl.md-systems"
version = "0.0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":engine:pipelines"))
    api(libs.kotlinx.html)
}

tasks.test {
    useJUnitPlatform()
}