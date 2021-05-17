plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)

    moduleDep(Modules.Data.Places.models)
    moduleDep(Modules.Data.Routing.models)

    moduleDep(Modules.Features.userLocation)
    moduleDep(Modules.Features.routing)
    moduleDep(Modules.Features.ecoParamElector)

    implementation(Deps.Compose.base())
}