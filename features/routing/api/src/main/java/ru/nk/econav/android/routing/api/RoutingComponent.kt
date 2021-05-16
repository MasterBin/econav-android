package ru.nk.econav.android.routing.api

import kotlinx.coroutines.flow.Flow
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

interface RoutingComponent {

    interface Dependencies {
        val getMapInterface : GetMapInterface
        val ecoParam : Flow<Float>
        val ecoParamRange : ClosedFloatingPointRange<Float>
        val routeReceived : OutEvent<Route>
            get() = OutEvent {  }

        val routeRequest : Flow<RouteReq>
    }

    fun interface Factory {
        fun invoke(componentContext: AppComponentContext, deps: Dependencies) : RoutingComponent
    }
}

data class RouteReq(
    val start : LatLon,
    val end : LatLon,
    val isStartFromUserLocation : Boolean
)