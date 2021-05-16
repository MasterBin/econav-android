plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.ui)
    moduleDep(Modules.Features.ecoParamElector)

    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Compose.allBase())
}
