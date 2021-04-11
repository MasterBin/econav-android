package ru.nk.econav.android.routing.api.model

import ru.nk.econav.core.common.models.LatLon

data class RouteRequest(
    val from : LatLon,
    val to : LatLon,
    val ecoParameter : Float,
    val parameterRange : ClosedFloatingPointRange<Float> = 0f..1f
)