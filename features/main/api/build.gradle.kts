plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Features.general)

    moduleDep(Modules.Features.placeDetail)
    moduleDep(Modules.Features.navigation)
    moduleDep(Modules.Features.chooseLocation)

    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}