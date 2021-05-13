package ru.nk.econav.android.data.routing.api

import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.core.common.models.LatLon

interface RoutingRepository {

    suspend fun getPath(start : LatLon, end : LatLon, ecoParam : Float) : Route

}