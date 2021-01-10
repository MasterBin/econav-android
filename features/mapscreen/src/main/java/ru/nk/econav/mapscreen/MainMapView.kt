package ru.nk.econav.mapscreen

import android.view.ViewGroup
import ru.nk.econav.core.common.view.Renderer
import ru.nk.econav.core.common.view.BaseUIComponent
import ru.nk.econav.core.common.view.render
import ru.nk.econav.core.common.viewbinding.viewBinding
import ru.nk.econav.mapscreen.databinding.ViewMainmapBinding

class Model
class Event

class MainMapView(parent : ViewGroup) : BaseUIComponent<Model, Event>() {

    private val vb by viewBinding(parent, ViewMainmapBinding::inflate)

    init {
        vb.run {

        }
    }

    override val renderer: Renderer<Model> = render {

    }
}