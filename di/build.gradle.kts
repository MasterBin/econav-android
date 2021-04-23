plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(Deps.koinScope)
    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())

    implementation(project(":core:common"))
    implementation(project(":core:network:api"))
    implementation(project(":core:network:impl"))

    implementation(project(":features:eco-param-elector:api"))
    implementation(project(":features:eco-param-elector:impl"))
    implementation(project(":features:main:api"))
    implementation(project(":features:main:impl"))
    implementation(project(":features:map:api"))
    implementation(project(":features:map:impl"))
    implementation(project(":features:routing:api"))
    implementation(project(":features:routing:impl"))
    implementation(project(":features:ecoplaces:api"))
    implementation(project(":features:ecoplaces:impl"))
}
