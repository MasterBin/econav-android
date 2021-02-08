package ru.nk.econav.android.map

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @POST("/route")
    suspend fun getPath(@Body rq : RouteRequest) : RouteResponse
}