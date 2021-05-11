package ru.nk.econav.android.features.main.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content

interface MainComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val routing : RoutingComponent.Factory
        val ecoParamElector : EcoParamElector.Factory
        val userLocation : UserLocationComponent.Factory
    }

    interface Dependencies {
        val getMapInterface : GetMapInterface
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, dependencies: Dependencies) : MainComponent
    }
}