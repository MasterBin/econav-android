plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    moduleDep(Modules.Core.common)
    implementation(Deps.kotlinSerialization)
}