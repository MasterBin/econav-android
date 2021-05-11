plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)

    moduleDep(Modules.Core.ui)
    moduleDep(Modules.Core.resources)

    moduleDep(Modules.Features.userLocation)

    implementation(Deps.coroutines)
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Compose.base())
    implementation(Deps.Activity.all())
    implementation(Deps.AndroidX.coreCtx)
}