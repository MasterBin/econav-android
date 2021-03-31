package ru.nk.econav.mapscreen.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PathRepositoryImpl(
    private val api: Api
) : PathRepository {

    override suspend fun getPath(start: LatLon, end: LatLon): RouteResponse =
        withContext(Dispatchers.IO) {
            api.getPath(
                RouteRequest(
                    start = start,
                    end = end,
                )
            )
        }
}