plugins {
    `android-base-lib-compose`
}

dependencies {

    moduleDep(Modules.Core.resources)

    implementation(Deps.Compose.allBase())
    implementation(Deps.Accompanist.systemUiController)
}
