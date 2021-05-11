package ru.nk.econav.android.data.routing.models

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val from : LatLon,
    val to : LatLon,
    val polyline : List<LatLon>,
    val distance : String,
    val time : String
)