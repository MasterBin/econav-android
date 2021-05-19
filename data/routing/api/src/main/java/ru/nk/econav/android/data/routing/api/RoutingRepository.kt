package ru.nk.econav.android.data.routing.api

import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.Upshot

interface RoutingRepository {

    suspend fun getPath(start : LatLon, end : LatLon, ecoParam : Float) : Upshot<Route,Error>

    sealed class Error {
        object NetworkOrServerError : Error()
        data class TextError(val text : String) : Error()
    }

}