import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions").version("0.38.0")
}

buildscript {
    val kotlinVersion by extra(Vers.KOTLIN)
    val compose_version by extra("1.0.0-beta01")
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
        classpath("com.google.gms:google-services:4.3.5")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/arkivanov/maven")
        maven(url = "https://dl.bintray.com/badoo/maven")
    }
}

task("clean") {
    delete(rootProject.buildDir)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
//    // Example 1: reject all non stable versions
//    rejectVersionIf {
//        isNonStable(candidate.version)
//    }

    // Example 2: disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

//    // Example 3: using the full syntax
//    resolutionStrategy {
//        componentSelection {
//            all {
//                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
//                    reject("Release candidate")
//                }
//            }
//        }
//    }
}