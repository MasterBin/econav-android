plugins {
    `android-base-lib`
}

dependencies {
    moduleDep(Modules.Core.common)
    moduleDep(Modules.Core.mapInterface)

    moduleDep(Modules.Data.Routing.models)

    implementation(Deps.coroutines)
}