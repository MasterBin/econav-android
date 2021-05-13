package ru.nk.econav.android.data.routing.models

import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class Route(
    val from : LatLon,
    val to : LatLon,
    val polyline : List<LatLon>,
    val distance : String,
    val time : String
)