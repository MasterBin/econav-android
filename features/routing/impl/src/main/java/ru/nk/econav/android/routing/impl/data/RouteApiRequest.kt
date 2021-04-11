package ru.nk.econav.android.routing.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class RouteApiRequest(
    @SerialName("start") val start : LatLon,
    @SerialName("end") val end : LatLon
)