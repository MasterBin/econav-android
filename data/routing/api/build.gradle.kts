plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Data.Routing.models)
    moduleDep(Modules.Core.common)

    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}