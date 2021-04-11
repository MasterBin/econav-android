plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":features:eco-param-elector:api"))

    implementation(Deps.Decompose.composeVariant())
    implementation(Deps.Compose.allBase())
}
