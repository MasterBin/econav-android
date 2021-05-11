package ru.nk.econav.android.ecoplaces.impl.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EcoPlace(
    @SerialName("location") val location : String
)