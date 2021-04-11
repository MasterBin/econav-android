plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:map-interface"))
    implementation(project(":core:ui"))

    implementation(project(":features:main:api"))
    implementation(project(":features:routing:api"))


    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.OSMDroid.osm)
    implementation(Deps.Compose.allBase())
}