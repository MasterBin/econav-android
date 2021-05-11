plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)
    moduleDep(Modules.Core.resources)

    moduleDep(Modules.Data.Routing.api)

    moduleDep(Modules.Features.routing)

    implementation(Deps.Decompose.common)
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}