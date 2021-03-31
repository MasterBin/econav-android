package ru.nk.econav.mapscreen

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.mapscreen.MainMapStore.*
import ru.nk.econav.mapscreen.data.LatLon
import ru.nk.econav.mapscreen.data.RouteResponse

interface MainMapStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SetStartPoint(val start : LatLon) : Intent()
        data class SetEndPoint(val endPoint : LatLon) : Intent()
    }

    data class State(
        val startPoint : LatLon? = null,
        val endPoint : LatLon? = null,
        val route : String? = null
    )

    sealed class Label {

    }
}
