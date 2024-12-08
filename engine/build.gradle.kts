import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinmultiplatform)
}

group = "nl.mdsystems"
version = "0.0.0.1"

kotlin {
    jvmToolchain(21)
    jvm() {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(project(":engine:threading"))
                api(project(":engine:logging"))
                api(project(":engine:metrics"))
                api(project(":engine:routing"))
                api(project(":engine:pipelines"))
                implementation(project(":utils"))
                
                implementation(libs.kotlin.coroutines)
            }
        }
    }
}