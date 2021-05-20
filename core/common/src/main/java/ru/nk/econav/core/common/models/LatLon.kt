package ru.nk.econav.core.common.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LatLon(
    @SerialName("lat") val lat : Double,
    @SerialName("lon") val lon : Double
) : Parcelable