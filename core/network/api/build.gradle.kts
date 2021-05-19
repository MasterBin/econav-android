plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Core.common)
    api(Deps.Retrofit.networkResponseAdapter)
    implementation(Deps.kotlinSerialization)
}