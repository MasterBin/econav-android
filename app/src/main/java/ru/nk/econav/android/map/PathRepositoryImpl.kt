package ru.nk.econav.android.map

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PathRepositoryImpl(
    private val api: Api
) : PathRepository {

    override fun getPath(start: LatLng, end: LatLng): Flow<RouteResponse> {
        return flow {
            emit(
                api.getPath(
                    RouteRequest(
                        start = LatLon(start.latitude, start.longitude),
                        end =  LatLon(end.latitude, end.longitude),
                    )
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}