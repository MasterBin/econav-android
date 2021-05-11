plugins {
    `android-base-lib-compose`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(Deps.coroutines)

    implementation(Deps.Compose.base())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.kotlinSerialization)
    implementation(Deps.Activity.all())
}