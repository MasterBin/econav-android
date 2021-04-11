package ru.nk.econav.core.common.models

import kotlinx.serialization.Serializable

@Serializable
data class LatLon(
    val lat : Double,
    val lon : Double
)