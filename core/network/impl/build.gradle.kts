plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.network)

    implementation(Deps.Retrofit.all())
    implementation(Deps.OkHttp.all())
    implementation(Deps.kotlinSerialization)
}