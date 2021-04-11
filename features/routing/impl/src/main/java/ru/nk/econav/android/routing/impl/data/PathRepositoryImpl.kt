package ru.nk.econav.android.routing.impl.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.android.routing.impl.util.PolylineUtil
import ru.nk.econav.core.common.models.LatLon

class PathRepositoryImpl(
    private val api: Api
) : PathRepository {

    override suspend fun getPath(start: LatLon, end: LatLon): Route =
        withContext(Dispatchers.IO) {
            val res = api.getPath(
                RouteApiRequest(
                    start = start,
                    end = end,
                )
            )

            Route(
                from = start,
                to = end,
                polyline = PolylineUtil.decode(res.encodedRoute)
            )
        }
}