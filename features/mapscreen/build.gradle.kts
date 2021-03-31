plugins {
    `android-base-lib`
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
}