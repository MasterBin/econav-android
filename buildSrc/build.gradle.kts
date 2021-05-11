import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    jcenter()
    google()
    mavenCentral()
}

dependencies {
    val kotlinVersion by extra("1.4.32")
    implementation("com.android.tools.build:gradle:7.0.0-alpha15")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}