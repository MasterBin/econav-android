import org.gradle.api.artifacts.dsl.DependencyHandler

object Deps {

    object AndroidX {
        const val coreCtx = "androidx.core:core-ktx:${Vers.CORE_CTX}"
        const val appCompat = "androidx.appcompat:appcompat:${Vers.APP_COMPAT}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Vers.CONSTRAINT_LAYOUT}"
        const val material = "com.google.android.material:material:${Vers.MATERIAL}"

        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Vers.SWIPE_REFRESH_LAYOUT}"
        const val preference = "androidx.preference:preference-ktx:${Vers.PREFERENCE}"

        object Lifecycle {
            private const val version = "2.3.0"

            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val common = "androidx.lifecycle:lifecycle-common-java8:$version"
        }
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

    object MVIKotlin {
        private const val version = "2.0.1"

        const val core = "com.arkivanov.mvikotlin:mvikotlin:$version"
        const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
        const val logging = "com.arkivanov.mvikotlin:mvikotlin-logging:$version"
        const val timeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$version"
        const val rx = "com.arkivanov.mvikotlin:rx:$version"
        const val coroutinesExt = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"

        fun base() = listOf(core, main, logging, timeTravel, coroutinesExt)
    }


    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val kotlinSerializationConverter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"

        fun base() = retrofit
        fun all() = listOf(retrofit, kotlinSerializationConverter)
    }

    object OkHttp {
        private const val version = "4.9.1"

        const val okHttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"

        fun base() = okHttp
        fun all() = listOf(okHttp, loggingInterceptor)
    }

    const val kotlinSerialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Vers.KOTLIN_SERIALIZATION}"

    const val osm = "org.osmdroid:osmdroid-android:${Vers.OSM}"

    const val desugarJDKLibs = "com.android.tools:desugar_jdk_libs:${Vers.DESUGAR_JDL_LIBS}"

    const val inputMask = "com.redmadrobot:input-mask-android:${Vers.INPUT_MASK}"
    const val switchButton = "com.github.zcweng:switch-button:${Vers.SWITCH_BUTTON}@aar"

    const val coil = "io.coil-kt:coil:${Vers.COIL}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Vers.COROUTINES}"

    const val photoView = "com.github.chrisbanes:PhotoView:${Vers.PHOTO_VIEW}"

    object Accompanist {
        private const val version = "0.7.0"

        const val insets = "com.google.accompanist:accompanist-insets:$version"
    }

    object Firebase {
        private const val version = "25.12.0"

        const val bom = "com.google.firebase:firebase-bom:$version"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-crashlytics-ktx"
    }

    object GoogleMap {
        const val maps = "com.google.android.gms:play-services-maps:17.0.0"

        private const val ktxVersion = "3.0.0"
        private const val utilVersion = "2.2.0"

        const val mapsKtx = "com.google.maps.android:maps-ktx:$ktxVersion"
        const val mapUtils = "com.google.maps.android:android-maps-utils:$utilVersion"
        const val mapUtilsKtx = "com.google.maps.android:maps-utils-ktx:$ktxVersion"

        fun all() = listOf(maps, mapsKtx, mapUtils, mapUtilsKtx)
    }

    object Decompose {
        private const val version = "0.2.1"

        const val common = "com.arkivanov.decompose:decompose:$version"
        const val androidExt = "com.arkivanov.decompose:extensions-android:$version"
        const val composeExt = "com.arkivanov.decompose:extensions-compose-jetpack:$version"

        fun composeVariant() = listOf(common, composeExt)
    }

    object Activity {
        private const val version = "1.3.0-alpha05"

        const val activity = "androidx.activity:activity:$version"
        const val ktx = "androidx.activity:activity-ktx:$version"
        const val composeExt = "androidx.activity:activity-compose:$version"

        fun all() = listOf(activity, ktx, composeExt)
    }

    object Compose {
        const val version = "1.0.0-beta03"

        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val materialIcons = "androidx.compose.material:material-icons-extended:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"

        fun allBase() = listOf(
            ui,
            material,
            materialIcons,
            tooling
        )
    }
}

fun DependencyHandler.desugaring() {
    coreLibraryDesugaring(Deps.desugarJDKLibs)
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