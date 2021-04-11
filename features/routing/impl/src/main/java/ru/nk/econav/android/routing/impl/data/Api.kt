package ru.nk.econav.android.routing.impl.data

import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("/api/route")
    suspend fun getPath(@Body rq : RouteApiRequest) : RouteResponse
}