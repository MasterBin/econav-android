package ru.nk.econav.android.map

import kotlinx.serialization.Serializable

@Serializable
data class RouteRequest(
    val start : LatLon,
    val end : LatLon
)

@Serializable
data class LatLon(
    val lat : Double,
    val lon : Double
)