package ru.nk.econav.android.ecoplaces.impl.data

import org.osmdroid.util.BoundingBox

interface EcoPlacesRepository {
    suspend fun getPlaces(box : BoundingBox) : List<EcoPlace>
}