package ru.nk.econav.android.map

import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val encodedRoute : String
)