plugins {
    `android-base-lib`
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:map-interface"))
    implementation(project(":features:eco-param-elector:api"))

    implementation(Deps.coroutines)
}