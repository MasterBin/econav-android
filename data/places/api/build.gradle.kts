plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(mapOf("path" to ":core:common")))
    moduleDep(Modules.Data.Places.models)

    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}