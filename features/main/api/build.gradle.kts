plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Features.routing)
    moduleDep(Modules.Features.ecoParamElector)
    moduleDep(Modules.Features.userLocation)



    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}