plugins {
    `android-base-lib`
}

dependencies {
    api(Deps.conductor)
    api(Deps.conductorViewPager)
    api(Deps.conductorTransactions)
    api(Deps.coroutines)
    api(Deps.koinScope)

    implementation("com.arkivanov.decompose:decompose:0.1.5")
    implementation("com.arkivanov.decompose:extensions-android:0.1.5")
}