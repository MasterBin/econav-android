package ru.nk.econav.android.general.api

import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

interface GeneralComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val userLocation : UserLocationComponent.Factory
        val searchPlaces : SearchPlacesComponent.Factory
    }

    interface Dependencies {
        val selectedStartLocation : Location
        val getMapInterface : GetMapInterface
        val placeSelected : OutEvent<Place>
        val chooseStartLocation : OutEvent<LatLon?> // LatLon - selected Location
        val chooseEndLocation : OutEvent<StartLocation> // LatLon - start Location
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : GeneralComponent
    }


    data class StartLocation(
        val startLocation: LatLon,
        val isStartLocationIsUserLocation : Boolean
    )

    data class Location(
        val location : LatLon? = null
    )

    data class Place(
        val startLocation : LatLon,
        val place : GeoFeature,
        val isStartLocationIsUserLocation : Boolean
    )
}