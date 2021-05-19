package ru.nk.econav.android.data.routing.impl

import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.data.routing.impl.api.Api
import ru.nk.econav.android.data.routing.impl.requests.RouteApiRequest
import ru.nk.econav.android.data.routing.impl.util.PolylineUtil
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.Upshot
import ru.nk.econav.core.common.util.asError
import ru.nk.econav.core.common.util.asOk

class RoutingRepositoryImpl(
    private val networking: Networking
) : RoutingRepository {

    private val api = networking.createApi(Api::class.java)

    override suspend fun getPath(start: LatLon, end: LatLon, ecoParam : Float): Upshot<Route, RoutingRepository.Error> =
        withContext(Dispatchers.IO) {
            val response = api.getPath(
                RouteApiRequest(
                    start = start,
                    end = end,
                    ecoParam = ecoParam
                )
            )

            when(response) {
                is NetworkResponse.Success -> {
                    val res = response.body
                    Route(
                        from = start,
                        to = end,
                        polyline = PolylineUtil.decode(res.encodedRoute),
                        distance = res.distance,
                        time = res.time,
                        instructions = res.instructions
                    ).asOk()
                }
                is NetworkResponse.UnknownError, is NetworkResponse.NetworkError -> {
                    RoutingRepository.Error.NetworkOrServerError.asError()
                }
                is NetworkResponse.ServerError -> {
                    RoutingRepository.Error.TextError(response.body?.text ?: "error").asError()
                }
            }
        }
}