import org.gradle.api.artifacts.dsl.DependencyHandler

object Deps {

    object AndroidX {
        const val coreCtx = "androidx.core:core-ktx:${Vers.CORE_CTX}"
        const val appCompat = "androidx.appcompat:appcompat:${Vers.APP_COMPAT}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Vers.CONSTRAINT_LAYOUT}"
        const val material = "com.google.android.material:material:${Vers.MATERIAL}"

        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Vers.SWIPE_REFRESH_LAYOUT}"
        const val preference = "androidx.preference:preference-ktx:${Vers.PREFERENCE}"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Vers.LIFECYCLE}"
        const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Vers.LIFECYCLE}"
    }

    object Testing {
        const val junit = "junit:junit:${Vers.JUNIT}"
        const val mockito = "org.mockito:mockito-core:${Vers.MOCKITO}"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Vers.LEAK_CANARY}"
    }

    const val badgeDrawable = "com.github.v2rc:badger:${Vers.BADGE_DRAWABLE}"

    const val timber = "com.jakewharton.timber:timber:${Vers.TIMBER}"

    const val koinScope = "org.koin:koin-androidx-scope:${Vers.KOIN}"

    const val epoxy = "com.airbnb.android:epoxy:${Vers.EPOXY}"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:${Vers.EPOXY}"

    const val conductor = "com.bluelinelabs:conductor:${Vers.CONDUCTOR}"
    const val conductorTransactions = "com.bluelinelabs:conductor-androidx-transition:${Vers.CONDUCTOR}"
    const val conductorViewPager = "com.bluelinelabs:conductor-viewpager:${Vers.CONDUCTOR}"

    const val runtimePermissions = "com.github.florent37:runtime-permission:${Vers.RUNTIME_PERMISSIONS}"

    const val mviKotlin = "com.arkivanov.mvikotlin:mvikotlin:${Vers.MVIKOTLIN}"
    const val mviKotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:${Vers.MVIKOTLIN}"
    const val mviKotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:${Vers.MVIKOTLIN}"
    const val mviKotlinTimetravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:${Vers.MVIKOTLIN}"
    const val mviKotlinRx = "com.arkivanov.mvikotlin:rx:${Vers.MVIKOTLIN}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Vers.RETROFIT}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Vers.OKHTTP}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Vers.RETROFIT}"
    const val retrofitRxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Vers.RETROFIT}"

    const val osm = "org.osmdroid:osmdroid-android:${Vers.OSM}"

    const val desugarJDKLibs = "com.android.tools:desugar_jdk_libs:${Vers.DESUGAR_JDL_LIBS}"

    const val inputMask = "com.redmadrobot:input-mask-android:${Vers.INPUT_MASK}"
    const val switchButton = "com.github.zcweng:switch-button:${Vers.SWITCH_BUTTON}@aar"

    const val coil = "io.coil-kt:coil:${Vers.COIL}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Vers.COROUTINES}"

    const val photoView = "com.github.chrisbanes:PhotoView:${Vers.PHOTO_VIEW}"

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Vers.FIREBASE_BOM}"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object GooglePlay {
        const val maps = "com.google.android.gms:play-services-maps:17.0.0"
        const val mapsKtx = "com.google.maps.android:maps-ktx:2.2.0"
        const val mapUtils = "com.google.maps.android:android-maps-utils:2.2.0"
        const val mapUtilsKtx = "com.google.maps.android:maps-utils-ktx:2.2.0"
    }

    object Decompose {
        const val decomposeCommon = "com.arkivanov.decompose:decompose:0.1.5"
        const val decomposeAndroidExt = "com.arkivanov.decompose:extensions-android:0.1.5"
    }
}

fun DependencyHandler.desugaring() {
    coreLibraryDesugaring(Deps.desugarJDKLibs)
}

fun DependencyHandler.mviKotlin() {
    implementation(Deps.mviKotlin)
    implementation(Deps.mviKotlinMain)
}

fun DependencyHandler.conductor() {
    implementation(Deps.conductor)
    implementation(Deps.conductorTransactions)
    implementation(Deps.conductorViewPager)
}

fun DependencyHandler.koin() {
    implementation(Deps.koinScope)
}

fun DependencyHandler.epoxy() {
    implementation(Deps.epoxy)
    kapt(Deps.epoxyProcessor)
}