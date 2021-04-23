package ru.nk.econav.android.ecoplaces.impl.data

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/api/places")
    suspend fun getEcoPlaces(
        @Query(value = "north", encoded = true) north : Double,
        @Query(value = "south", encoded = true) south : Double,
        @Query(value = "east", encoded = true) east : Double,
        @Query(value = "west", encoded = true) west : Double,
    ) : List<EcoPlace>
}