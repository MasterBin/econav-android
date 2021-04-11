package ru.nk.econav.android.features.main.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.decopmose.Content

interface MainComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val routing : RoutingComponent.Factory
    }

    interface Dependencies {
        val getMapInterface : GetMapInterface
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, dependencies: Dependencies) : MainComponent
    }
}