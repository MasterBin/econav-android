plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:map-interface"))
    implementation(project(":features:map:api"))
    implementation(project(":features:main:api"))

    implementation(Deps.OSMDroid.all())
    implementation(Deps.Compose.allBase())
    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.AndroidX.preference)
}
