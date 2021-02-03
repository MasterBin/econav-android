package ru.nk.econav.android.map

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface PathRepository {

    fun getPath(start : LatLng, end : LatLng) : Flow<List<String>>
}