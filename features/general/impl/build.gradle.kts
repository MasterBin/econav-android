plugins {
    `android-base-lib-compose`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Features.general)

    implementation(Deps.Decompose.common)
    implementation(Deps.Compose.base())
}