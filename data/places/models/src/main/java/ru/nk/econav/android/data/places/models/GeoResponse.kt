package ru.nk.econav.android.data.places.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class GeoResponse(
    @SerialName("features") val features : List<GeoFeature>
)

@Serializable
data class GeoFeature(
    @SerialName("name")val name : String,
    @SerialName("address")val address : String,
    @SerialName("center")val center : LatLon,
    @SerialName("bbox") val bbox : BoundingBox?,
    @SerialName("matchingText") val matchingText : String?,
    @SerialName("distanceTo") val distanceTo : String?
)

@Serializable
data class BoundingBox(
    val east : Double,
    val north : Double,
    val west : Double,
    val south : Double
)