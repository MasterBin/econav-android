package ru.nk.econav.android.core.mapinterface

import android.content.Context
import com.arkivanov.decompose.lifecycle.Lifecycle
import org.osmdroid.views.overlay.Overlay

interface MapInterface {
    fun add(overlay : Overlay)
    fun remove(overlay : Overlay)
    fun invalidateMap()
}

typealias GetMapInterface = (lifecycle : Lifecycle) -> MapInterface