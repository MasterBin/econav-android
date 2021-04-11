package ru.nk.econav.mapscreen.data

import kotlinx.coroutines.flow.Flow

interface PathRepository {

    suspend fun getPath(start : LatLon, end : LatLon) : RouteResponse
}