plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    val kotlinVersion by extra("1.4.31")
    implementation("com.android.tools.build:gradle:7.0.0-alpha12")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}