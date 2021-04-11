package ru.nk.econav.mapscreen.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @POST("/route")
    suspend fun getPath(@Body rq : RouteRequest) : RouteResponse
}