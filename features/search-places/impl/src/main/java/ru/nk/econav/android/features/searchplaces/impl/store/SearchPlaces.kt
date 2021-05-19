package ru.nk.econav.android.features.searchplaces.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.impl.store.SearchPlacesStore.*
import ru.nk.econav.core.common.models.LatLon

interface SearchPlacesStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SearchTextChanged(val searchText: String) : Intent()
        object SearchClicked : Intent()
        data class UserLocationChanged(val location : LatLon) : Intent()
    }

    data class State(
        val foundFeatures : List<GeoFeature>? = null,
        val searchText : String = "",
        val userLocation : LatLon? = null
    )

    sealed class Label {
        object NetworkError : Label()
        data class TextError(val text : String) : Label()
    }
}
