plugins {
    `android-base-lib-compose`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":features:extended-lifecycle:api"))

    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Compose.allBase())
    implementation(Deps.Retrofit.base())
    implementation(Deps.kotlinSerialization)
    implementation(Deps.Activity.all())
    implementation(Deps.GoogleMap.all())

    implementation(Deps.OSMDroid.all())

    implementation(Deps.AndroidX.preference)

    androidTestImplementation(Deps.Testing.jetpackComposeUITest)
    testImplementation(Deps.Testing.junit)
}