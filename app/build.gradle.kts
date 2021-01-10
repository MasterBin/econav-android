import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
//    id("com.google.firebase.crashlytics")
//    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
//    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        applicationId = AndroidConfig.APPLICATION_ID
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mapKey = gradleLocalProperties(rootDir).getProperty("MAPS_API_KEY")
        manifestPlaceholders["mapsApiKey"] = mapKey
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
//            firebaseCrashlytics {
//                mappingFileUploadEnabled=true
//            }
        }
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
        maybeCreate("debug-minify")
        getByName("debug-minify") {
            initWith(getByName("debug"))
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            matchingFallbacks += "debug"
//            firebaseCrashlytics {
//                mappingFileUploadEnabled=true
//            }
        }
        maybeCreate("leakCanary")
        getByName("leakCanary") {
            initWith(getByName("debug"))
            matchingFallbacks += "debug"
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
//    implementation(platform(Deps.Firebase.bom))
//    implementation(Deps.Firebase.crashlytics)
//    implementation(Deps.Firebase.analytics)

    implementation(Deps.Decompose.decomposeCommon)
    implementation(Deps.Decompose.decomposeAndroidExt)

    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    implementation(project(":core:common"))
    implementation(project(":features:extended-lifecycle:impl"))

    implementation(Deps.timber)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.coreCtx)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.swipeRefreshLayout)
    implementation(Deps.coil)

    implementation(Deps.GooglePlay.maps)

    testImplementation(Deps.Testing.junit)
    "leakCanaryImplementation"(Deps.Testing.leakCanary)
}
