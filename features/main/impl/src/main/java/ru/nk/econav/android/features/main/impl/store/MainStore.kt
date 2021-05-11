package ru.nk.econav.android.features.main.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.features.main.impl.store.MainStore.*

interface MainStore : Store<Intent, State, Label> {

    sealed class Intent {
        object CreateRouteByPlacingPoints : Intent()
        object Cancel : Intent()
        data class ChangeEcoParam(val ecoParam: Float) : Intent()
        data class RouteDetailsReceived(val distance : String, val time : String) : Intent()
    }

    data class State(
        val routingState: Routing = Routing.General,
        val ecoParam: Float = 1f,
    ) {
        sealed class Routing {
            object General : Routing()
            object PlacingPoints : Routing()
            data class Route(val distance : String, val time : String) : Routing()
        }
    }

    sealed class Label {
    }
}
