import org.jetbrains.kotlin.backend.jvm.codegen.ClassCodegen.Companion.getOrCreate

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "ru.nk.econav.uicomponents"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Compose.version
    }
}

dependencies {

    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:9.6.1")
    implementation(project(":core:ui"))
    implementation(Deps.timber)
    implementation(Deps.Compose.allBase())
    implementation(Deps.Activity.all())
}