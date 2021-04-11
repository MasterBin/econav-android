package ru.nk.econav.android.di

import ru.nk.econav.android.di.core.moduleNetwork
import ru.nk.econav.android.di.features.moduleEcoParamElector
import ru.nk.econav.android.di.features.moduleMain
import ru.nk.econav.android.di.features.moduleMap
import ru.nk.econav.android.di.features.moduleRouting

val modules = listOf(
    moduleNetwork,

    moduleEcoParamElector,
    moduleMain,
    moduleMap,
    moduleRouting
)