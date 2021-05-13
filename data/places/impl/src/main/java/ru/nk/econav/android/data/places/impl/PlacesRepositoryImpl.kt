package ru.nk.econav.android.data.places.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.impl.api.Api
import ru.nk.econav.android.data.places.models.GeoResponse
import ru.nk.econav.core.common.models.LatLon

class PlacesRepositoryImpl(
    private val networking: Networking
) : PlacesRepository {

    private val api = networking.createApi(Api::class.java)

    override suspend fun search(
        query: String,
        autoComplete: Boolean,
        userLocation: LatLon?
    ): GeoResponse = withContext(Dispatchers.Default) {
        api.search(
            autoComplete,
            query,
            userLatitude = userLocation?.lat,
            userLongitude = userLocation?.lon
        )
    }


}