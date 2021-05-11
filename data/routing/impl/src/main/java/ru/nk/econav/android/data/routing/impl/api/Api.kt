package ru.nk.econav.android.data.routing.impl.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.nk.econav.android.data.routing.impl.requests.RouteApiRequest
import ru.nk.econav.android.data.routing.impl.responses.RouteResponse

interface Api {

    @POST("/api/route")
    suspend fun getPath(@Body rq : RouteApiRequest) : RouteResponse
}