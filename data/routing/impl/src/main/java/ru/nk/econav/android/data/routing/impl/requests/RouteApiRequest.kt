package ru.nk.econav.android.data.routing.impl.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class RouteApiRequest(
    @SerialName("start") val start : LatLon,
    @SerialName("end") val end : LatLon,
    @SerialName("ecoParam") val ecoParam : Float
)
