package ru.nk.econav.android.routing.impl.data

import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.core.common.models.LatLon

interface PathRepository {

    suspend fun getPath(start : LatLon, end : LatLon) : Route
}