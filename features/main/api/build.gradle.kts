plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Features.general)

    moduleDep(Modules.Features.placeDetail)

    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}