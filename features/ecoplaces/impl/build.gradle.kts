plugins {
    `android-base-lib-compose`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":features:ecoplaces:api"))

    implementation(project(":core:common"))
    implementation(project(":core:map-interface"))
    implementation(project(":core:network:api"))

    implementation(Deps.kotlinSerialization)
    implementation(Deps.Retrofit.base())
    implementation(Deps.Compose.base())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.OSMDroid.all())
}
