plugins {
    `android-base-lib`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network:api"))

    implementation(Deps.Retrofit.all())
    implementation(Deps.OkHttp.all())
    implementation(Deps.kotlinSerialization)
}