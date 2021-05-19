package ru.nk.econav.android.routing.impl

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.routing.api.RouteReq
import ru.nk.econav.android.routing.impl.RoutingStore.*
import ru.nk.econav.core.common.models.LatLon

interface RoutingStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class RouteRequest(val req : RouteReq) : Intent()
        object Cancel : Intent()
        data class ChangeEcoParam(val ecoParam: Float) : Intent()
    }

    data class State(
        val routingState: Routing = Routing.General,
        val ecoParam: Float = 1f,
    ) {
        sealed class Routing {
            object General : Routing()

            data class RouteCreated(
                val route: Route,
                val routeReq: RouteReq
            ) : Routing()
        }
    }

    sealed class Label {
        object NetworkError : Label()
        data class TextError(val text : String) : Label()
    }
}
