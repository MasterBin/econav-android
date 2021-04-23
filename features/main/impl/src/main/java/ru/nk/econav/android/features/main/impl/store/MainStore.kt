package ru.nk.econav.android.features.main.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.features.main.impl.store.MainStore.*
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.core.common.models.LatLon

interface MainStore : Store<Intent, State, Label> {

    sealed class Intent {
        object CreateRouteByPlacingPoints : Intent()
        object Cancel : Intent()
        data class PlacePoint(val point: LatLon) : Intent()
        data class RouteReceived(val route: Route) : Intent()
        data class ChangeEcoParam(val ecoParam: Float) : Intent()
    }

    data class State(
        val routingState: Routing = Routing.General,
        val ecoParam: Float = 1f
    ) {
        sealed class Routing {
            object General : Routing()

            data class CreateRouteByPlacingPoints(
                val startPoint: LatLon? = null,
                val endPoint: LatLon? = null,
            ) : Routing()

            data class RouteCreated(
                val route: Route
            ) : Routing()
        }
    }

    sealed class Label {
        data class RequestRoute(
            val startPoint: LatLon,
            val endPoint: LatLon,
            val ecoParam : Float
        ) : Label()
    }
}
