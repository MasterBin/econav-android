plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:map-interface"))
    implementation(project(":features:routing:api"))
    implementation(project(":features:ecoplaces:api"))

    implementation(project(":features:eco-param-elector:api"))


    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}