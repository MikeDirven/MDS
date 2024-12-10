plugins {
    alias(libs.plugins.kotlinjvm)
    application
}

group = "nl.md-systems"
version = "0.0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":engine"))
    implementation(project(":engine:modules"))
    implementation(project(":engine:logging"))
    implementation(project(":engine:metrics"))
    implementation(project(":engine:routing"))
    implementation(project(":plugins:templating-kotlinx-html"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}