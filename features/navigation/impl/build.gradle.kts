plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Core.ui)
    moduleDep(Modules.Core.resources)

    moduleDep(Modules.Data.Places.models)
    moduleDep(Modules.Data.Routing.models)

    moduleDep(Modules.Features.navigation)
    moduleDep(Modules.Features.userLocation)

    implementation(Deps.OSMDroid.all())
    implementation(Deps.Compose.allBase())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Accompanist.insets)
}
