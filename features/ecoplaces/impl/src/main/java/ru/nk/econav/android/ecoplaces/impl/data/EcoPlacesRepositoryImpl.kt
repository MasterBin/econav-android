package ru.nk.econav.android.ecoplaces.impl.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.BoundingBox

class EcoPlacesRepositoryImpl(
    private val api : Api
) : EcoPlacesRepository {
    override suspend fun getPlaces(box: BoundingBox): List<EcoPlace> = withContext(Dispatchers.IO) {
        api.getEcoPlaces(
            north = box.latNorth,
            south = box.latSouth,
            west = box.lonWest,
            east = box.lonEast
        )
    }
}