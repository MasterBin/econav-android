package ru.nk.econav.android.data.routing.models

import kotlinx.serialization.Serializable

@Serializable
data class LatLon(
    val lat : Double,
    val lon : Double
)