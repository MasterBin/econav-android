package ru.nk.econav.android.core.mapinterface

import android.content.Context
import com.arkivanov.decompose.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.overlay.Overlay

interface MapInterface {
    fun add(overlay : Overlay)
    fun remove(overlay : Overlay)
    fun invalidateMap()


    val boundingBoxUpdateFlow : Flow<BoundingBoxUpdate>
}

data class BoundingBoxUpdate(
    val boundingBox: BoundingBox,
    val fromZoom : Boolean
)

typealias GetMapInterface = (lifecycle : Lifecycle) -> MapInterface