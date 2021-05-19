package ru.nk.econav.android.data.places.impl

import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.impl.api.Api
import ru.nk.econav.android.data.places.models.GeoResponse
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.Upshot
import ru.nk.econav.core.common.util.asError
import ru.nk.econav.core.common.util.asOk

class PlacesRepositoryImpl(
    private val networking: Networking
) : PlacesRepository {

    private val api = networking.createApi(Api::class.java)

    override suspend fun search(
        query: String,
        autoComplete: Boolean,
        userLocation: LatLon?
    ): Upshot<GeoResponse, PlacesRepository.Error> = withContext(Dispatchers.Default) {
        val res = api.search(
            autoComplete,
            query,
            userLatitude = userLocation?.lat,
            userLongitude = userLocation?.lon
        )

        when(res) {
            is NetworkResponse.Success -> {
                res.body.asOk()
            }
            is NetworkResponse.UnknownError, is NetworkResponse.NetworkError -> {
                PlacesRepository.Error.NetworkOrServerError.asError()
            }
            is NetworkResponse.ServerError -> {
                PlacesRepository.Error.TextError(res.body?.text ?: "error").asError()
            }
        }
    }


}