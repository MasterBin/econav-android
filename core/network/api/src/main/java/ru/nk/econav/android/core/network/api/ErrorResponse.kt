package ru.nk.econav.android.core.network.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ErrorResponse(
    @SerialName("text") val text : String
)