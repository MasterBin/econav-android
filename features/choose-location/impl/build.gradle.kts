plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Core.ui)
    moduleDep(Modules.Core.resources)

    moduleDep(Modules.Features.chooseLocation)
    moduleDep(Modules.Features.searchPlaces)

    implementation(Deps.OSMDroid.all())
    implementation(Deps.Compose.allBase())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Accompanist.insets)
}
