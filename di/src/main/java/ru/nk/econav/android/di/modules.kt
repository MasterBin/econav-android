package ru.nk.econav.android.di

import ru.nk.econav.android.di.core.moduleNetwork
import ru.nk.econav.android.di.features.*

val modules = listOf(
    moduleNetwork,

    moduleEcoParamElector,
    moduleMain,
    moduleMap,
    moduleRouting,
    moduleEcoPlaces
)