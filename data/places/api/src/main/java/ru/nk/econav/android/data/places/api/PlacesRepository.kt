package ru.nk.econav.android.data.places.api

import ru.nk.econav.android.data.places.models.GeoResponse
import ru.nk.econav.core.common.models.LatLon

interface PlacesRepository {

    suspend fun search(query : String, autoComplete : Boolean, userLocation : LatLon?) : GeoResponse
}