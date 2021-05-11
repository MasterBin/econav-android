plugins {
    `android-base-lib`
}

dependencies {
    api(Deps.OSMDroid.osm)
    implementation(Deps.Decompose.common)
    implementation(Deps.coroutines)
}
