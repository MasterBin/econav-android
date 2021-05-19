package ru.nk.econav.android.data.places.api

import ru.nk.econav.android.data.places.models.GeoResponse
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.Upshot

interface PlacesRepository {

    suspend fun search(query : String, autoComplete : Boolean, userLocation : LatLon?) : Upshot<GeoResponse, Error>

    sealed class Error {
        object NetworkOrServerError : Error()
        data class TextError(val text : String) : Error()
    }
}