package ru.nk.econav.android.data.places.impl.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nk.econav.android.data.places.models.GeoResponse

interface Api {

    @GET("/api/geocoding/search")
    suspend fun search(
        @Query("autoComplete") autoComplete : Boolean,
        @Query("query") query : String,
        @Query("userLatitude") userLatitude : Double? = null,
        @Query("userLongitude") userLongitude : Double? = null
    ) : GeoResponse

}