plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Features.userLocation)
    moduleDep(Modules.Features.searchPlaces)

    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}