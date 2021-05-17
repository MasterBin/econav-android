package ru.nk.econav.android.data.routing.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.core.common.models.LatLon

@Serializable
@Parcelize
data class Route(
    val from : LatLon,
    val to : LatLon,
    val polyline : List<LatLon>,
    val distance : String,
    val time : String,
    val instructions : List<Instruction>
) : Parcelable

@Serializable
@Parcelize
data class Instruction(
    @SerialName("turnDescription") val turnDescription : String,
    @SerialName("distance") val distance : String,
    @SerialName("time") val time : String,
    @SerialName("points") val points : List<LatLon>
) : Parcelable