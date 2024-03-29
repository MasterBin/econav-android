plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(Deps.koin)
    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())

    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.network)
    moduleDep(ModulesUnsafe.Core.networkImpl)

    moduleDep(Modules.Data.Routing.api)
    moduleDep(ModulesUnsafe.Data.Routing.impl)

    moduleDep(Modules.Data.Places.api)
    moduleDep(ModulesUnsafe.Data.Places.impl)

    moduleDep(Modules.Features.ecoParamElector)
    moduleDep(ModulesUnsafe.Features.ecoParamElectorImpl)

    moduleDep(Modules.Features.main)
    moduleDep(ModulesUnsafe.Features.mainImpl)

    moduleDep(Modules.Features.map)
    moduleDep(ModulesUnsafe.Features.mapImpl)

    moduleDep(Modules.Features.userLocation)
    moduleDep(ModulesUnsafe.Features.userLocationImpl)

    moduleDep(Modules.Features.routing)
    moduleDep(ModulesUnsafe.Features.routingImpl)

    moduleDep(Modules.Features.general)
    moduleDep(ModulesUnsafe.Features.generalImpl)

    moduleDep(Modules.Features.searchPlaces)
    moduleDep(ModulesUnsafe.Features.searchPlaces)

    moduleDep(Modules.Features.placeDetail)
    moduleDep(ModulesUnsafe.Features.placeDetail)

    moduleDep(Modules.Features.navigation)
    moduleDep(ModulesUnsafe.Features.navigation)

    moduleDep(Modules.Features.chooseLocation)
    moduleDep(ModulesUnsafe.Features.chooseLocation)
}
