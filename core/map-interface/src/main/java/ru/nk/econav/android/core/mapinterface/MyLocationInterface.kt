package ru.nk.econav.android.core.mapinterface

import android.graphics.Bitmap
import android.location.Location
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

interface MyLocationInterface {
    fun setUserIcon(icon : Bitmap)
    fun setArrowIcon(personIcon : Bitmap, userIcon : Bitmap)

    fun getMyLocation() : GeoPoint?
    fun enableFollowLocation()
    fun disableFollowLocation()
    fun isFollowLocationEnabled() : Boolean?

    fun enableMyLocation(myLocationProvider: IMyLocationProvider)
    fun disableMyLocation()
}