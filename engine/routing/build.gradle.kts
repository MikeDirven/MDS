plugins {
    alias(libs.plugins.kotlinmultiplatform)
}

group = "nl.mdsystems"
version = "0.0.0.1"

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
                implementation(project(":engine:logging"))
                implementation(project(":engine:pipelines"))
                implementation(libs.kotlin.coroutines)
            }
        }
    }
}