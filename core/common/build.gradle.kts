plugins {
    `android-base-lib`
}

dependencies {
    implementation(Deps.coroutines)

    implementation(Deps.Decompose.common)
    implementation(Deps.MVIKotlin.base())
    implementation(Deps.Activity.all())
}