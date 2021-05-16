package ru.nk.econav.core.common.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LatLon(
    val lat : Double,
    val lon : Double
) : Parcelable