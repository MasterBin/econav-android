package ru.nk.econav.android.routing.impl.util

import org.osmdroid.util.GeoPoint
import ru.nk.econav.core.common.models.LatLon

fun GeoPoint.toLatLon() = LatLon(
    lat = latitude,
    lon = longitude
)

fun LatLon.toGeoPoint() = GeoPoint(lat, lon)