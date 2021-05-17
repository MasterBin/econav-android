package ru.nk.econav.android.data.routing.impl.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.nk.econav.android.data.routing.models.Instruction

@Serializable
data class RouteResponse(
    @SerialName("encodedRoute") val encodedRoute : String,
    @SerialName("distance") val distance : String,
    @SerialName("time") val time : String,
    @SerialName("instructions") val instructions : List<Instruction>
)