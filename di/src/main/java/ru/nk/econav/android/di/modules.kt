package ru.nk.econav.android.di

import ru.nk.econav.android.di.core.moduleNetwork
import ru.nk.econav.android.di.data.moduleDataPlaces
import ru.nk.econav.android.di.data.moduleDataRouting
import ru.nk.econav.android.di.features.*

val modules = listOf(
    moduleNetwork,

    moduleDataRouting,
    moduleDataPlaces,

    moduleEcoParamElector,
    moduleMain,
    moduleMap,
    moduleRouting,
    moduleUserLocation,
    moduleGeneral,
    moduleSearchPlaces,
    modulePlaceDetails,
    moduleNavigation,
    moduleChooseLocation
)