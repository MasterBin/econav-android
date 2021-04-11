plugins {
    `android-base-lib-compose`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(Deps.coroutines)

    implementation(Deps.Decompose.common)
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Activity.all())
    implementation(Deps.kotlinSerialization)
}