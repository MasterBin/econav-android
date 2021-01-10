plugins {
    `android-base-lib`
}

dependencies {
    implementation(project(":core:common"))
    api(project(":features:extended-lifecycle:api"))

    implementation(Deps.Decompose.decomposeCommon)
    implementation(Deps.Decompose.decomposeAndroidExt)
}