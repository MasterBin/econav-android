plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDepApi(Modules.Data.Places.models)

    implementation(Deps.Compose.base())
}