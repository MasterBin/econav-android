package ru.nk.econav.android.features.navigation.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.util.OutEvent

interface NavigationComponent {

    fun render(modifier : Modifier) : Content

    interface Dependencies {
        val getMapInterface : GetMapInterface
        val route : Route
        val permissionNotGranted : OutEvent<Unit>
    }

    interface Children {
        val userLocationComponent : UserLocationComponent.Factory
    }

    fun interface Factory{
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : NavigationComponent
    }

}