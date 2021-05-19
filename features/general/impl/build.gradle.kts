plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.ui)
    moduleDep(Modules.Core.resources)

    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Features.userLocation)
    moduleDep(Modules.Features.general)
    moduleDep(Modules.Features.searchPlaces)

    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.OSMDroid.osm)
    implementation(Deps.Compose.allBase())
    implementation(Deps.Accompanist.insets)
}