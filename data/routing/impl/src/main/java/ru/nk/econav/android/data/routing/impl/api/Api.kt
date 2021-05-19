package ru.nk.econav.android.data.routing.impl.api

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST
import ru.nk.econav.android.core.network.api.ErrorResponse
import ru.nk.econav.android.data.routing.impl.requests.RouteApiRequest
import ru.nk.econav.android.data.routing.impl.responses.RouteResponse

interface Api {

    @POST("/api/route")
    suspend fun getPath(@Body rq : RouteApiRequest) : NetworkResponse<RouteResponse, ErrorResponse>
}