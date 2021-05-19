plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)

    moduleDep(Modules.Features.searchPlaces)

    implementation(Deps.Compose.base())
}