package ru.nk.econav.android.ecoplaces.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class EcoPlace(
    @SerialName("location") val location : LatLon
)