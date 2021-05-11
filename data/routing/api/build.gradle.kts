plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Data.Routing.models)

    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}