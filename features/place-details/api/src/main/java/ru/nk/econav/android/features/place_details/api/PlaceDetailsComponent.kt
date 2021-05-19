package ru.nk.econav.android.features.place_details.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

interface PlaceDetailsComponent {

    fun render(modifier : Modifier) : Content

    interface Dependencies {
        val place : GeoFeature
        val startLocation : LatLon
        val getMapInterface : GetMapInterface
        val navigateTo : OutEvent<Route>
        val isStartLocationIsUserLocation : Boolean
    }

    interface Children {
        val userLocation : UserLocationComponent.Factory
        val ecoParamElector : EcoParamElector.Factory
        val routing : RoutingComponent.Factory
    }

    fun interface Factory {
        fun invoke(appComponentContext : AppComponentContext, deps : Dependencies) : PlaceDetailsComponent
    }
}