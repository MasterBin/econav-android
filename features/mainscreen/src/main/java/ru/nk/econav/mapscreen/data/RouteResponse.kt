package ru.nk.econav.mapscreen.data

import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val encodedRoute : String
)