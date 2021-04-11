plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network:api"))
    implementation(project(":core:map-interface"))
    implementation(project(":features:routing:api"))

    implementation(Deps.Retrofit.base())
    implementation(Deps.Decompose.common)
    implementation(Deps.coroutines)
    implementation(Deps.kotlinSerialization)
}