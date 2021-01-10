plugins {
    id("com.github.ben-manes.versions").version("0.36.0")
}

buildscript {
    val kotlinVersion by extra( "1.4.21")
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.3.0")
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