package ru.nk.econav.android.data.routing.api

import ru.nk.econav.android.data.routing.models.LatLon
import ru.nk.econav.android.data.routing.models.Route

interface RoutingRepository {

    suspend fun getPath(start : LatLon, end : LatLon, ecoParam : Float) : Route

}