package ru.nk.econav.mapscreen

import org.osmdroid.views.overlay.Overlay

interface MapInterface {
    fun add(overlay : Overlay)
    fun remove(overlay : Overlay)
}