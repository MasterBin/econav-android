package ru.nk.econav.android.ecoplaces.api

import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.core.common.decompose.AppComponentContext

interface EcoPlacesComponent {

    interface Dependencies {
        val getMapInterface : GetMapInterface
    }

    fun interface Factory {
        fun invoke(componentContext : AppComponentContext, deps : Dependencies) : EcoPlacesComponent
    }
}