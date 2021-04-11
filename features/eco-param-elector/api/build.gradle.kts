plugins {
    `android-base-lib-compose`
}

dependencies {
    implementation(project(":core:common"))
    implementation(Deps.Compose.base())
}
