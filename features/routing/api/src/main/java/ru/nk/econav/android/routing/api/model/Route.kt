package ru.nk.econav.android.routing.api.model

import ru.nk.econav.core.common.models.LatLon

data class Route(
    val from : LatLon,
    val to : LatLon,
    val polyline : List<LatLon>
)