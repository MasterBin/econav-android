plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)

    moduleDep(Modules.Features.map)
    moduleDep(Modules.Features.main)


    implementation(Deps.OSMDroid.all())
    implementation(Deps.Compose.allBase())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.AndroidX.preference)
}
