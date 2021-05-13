package ru.nk.econav.android.data.routing.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.data.routing.impl.api.Api
import ru.nk.econav.android.data.routing.impl.requests.RouteApiRequest
import ru.nk.econav.android.data.routing.impl.util.PolylineUtil
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.core.common.models.LatLon

class RoutingRepositoryImpl(
    private val networking: Networking
) : RoutingRepository {

    private val api = networking.createApi(Api::class.java)

    override suspend fun getPath(start: LatLon, end: LatLon, ecoParam : Float): Route =
        withContext(Dispatchers.IO) {
            val res = api.getPath(
                RouteApiRequest(
                    start = start,
                    end = end,
                    ecoParam = ecoParam
                )
            )

            Route(
                from = start,
                to = end,
                polyline = PolylineUtil.decode(res.encodedRoute),
                distance = res.distance,
                time = res.time
            )
        }
}