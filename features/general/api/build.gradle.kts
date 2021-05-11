plugins {
    `android-base-lib-compose`
}

dependencies {

    moduleDep(Modules.Core.common)

    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}