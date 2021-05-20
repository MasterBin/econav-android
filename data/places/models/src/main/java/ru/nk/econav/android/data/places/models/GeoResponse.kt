package ru.nk.econav.android.data.places.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
data class GeoResponse(
    @SerialName("features") val features : List<GeoFeature>
)

@Serializable
@Parcelize
data class GeoFeature(
    @SerialName("name")val name : String,
    @SerialName("address")val address : String,
    @SerialName("center")val center : LatLon,
    @SerialName("bbox") val bbox : BoundingBox?,
    @SerialName("matchingText") val matchingText : String?,
    @SerialName("distanceTo") val distanceTo : String?
) : Parcelable

@Serializable
@Parcelize
data class BoundingBox(
    @SerialName("east") val east : Double,
    @SerialName("north") val north : Double,
    @SerialName("west") val west : Double,
    @SerialName("south") val south : Double
) : Parcelable