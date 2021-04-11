package ru.nk.econav.android.routing.impl.data

import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val encodedRoute : String
)