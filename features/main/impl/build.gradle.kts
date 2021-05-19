plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Core.ui)

    moduleDep(Modules.Features.main)
    moduleDep(Modules.Features.general)
    moduleDep(Modules.Features.placeDetail)
    moduleDep(Modules.Features.navigation)
    moduleDep(Modules.Features.chooseLocation)

    moduleDep(Modules.Data.Places.models)
    moduleDep(Modules.Data.Routing.models)

    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.OSMDroid.osm)
    implementation(Deps.Compose.allBase())
    implementation(Deps.Accompanist.insets)
}