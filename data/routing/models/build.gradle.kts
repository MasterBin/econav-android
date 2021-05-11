plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(Deps.kotlinSerialization)
}