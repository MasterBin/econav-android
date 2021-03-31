plugins {
    `android-base-lib`
}

dependencies {
    implementation(project(":core:common"))
    api(project(":features:extended-lifecycle:api"))

    implementation(Deps.koinScope)
    implementation(Deps.Decompose.common)
    implementation(Deps.Decompose.androidExt)
    implementation(Deps.Activity.all())
}