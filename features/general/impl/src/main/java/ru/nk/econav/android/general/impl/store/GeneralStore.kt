package ru.nk.econav.android.general.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.general.impl.store.GeneralStore.*
import ru.nk.econav.core.common.models.LatLon

interface GeneralStore : Store<Intent, State, Label> {

    sealed class Intent {
        object PermissionNotGranted : Intent()
        object ShowUserLocationButton : Intent()
        data class SetUserLocation(val location : LatLon) : Intent()
        data class SelectedStartLocation(val location : LatLon) : Intent()
        data class SelectedPlace(val place : GeoFeature) : Intent()
        data class SearchTextChanged(val text : String) : Intent()
    }

    data class State(
        val startLocation : LatLon? = null,
        val isUserLocation : Boolean = false,
        val searchText : String = "",
        val showUserLocationButton : Boolean = false
    )

    sealed class Label {
        data class RouteToPlace(val startLocation: LatLon, val place : GeoFeature, val isUserLocation : Boolean) : Label()
        object NoStartLocation: Label()
    }
}
