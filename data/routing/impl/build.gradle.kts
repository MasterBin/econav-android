plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Data.Routing.api)
    moduleDep(Modules.Core.network)

    implementation(Deps.Retrofit.base())
    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}