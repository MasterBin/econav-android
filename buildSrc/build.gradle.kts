plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    val kotlinVersion by extra("1.4.32")
    implementation("com.android.tools.build:gradle:7.0.0-alpha14")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}