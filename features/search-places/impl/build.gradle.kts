plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.ui)

    moduleDep(Modules.Data.Places.api)

    moduleDep(Modules.Features.searchPlaces)

    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Compose.allBase())
}