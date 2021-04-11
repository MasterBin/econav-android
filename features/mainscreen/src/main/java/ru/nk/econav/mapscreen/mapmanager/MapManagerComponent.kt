package ru.nk.econav.mapscreen.mapmanager

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.lifecycle.Lifecycle
import ru.nk.econav.mapscreen.MapInterface
import ru.nk.econav.mapscreen.map.MapComponent

class MapManagerComponent(
    private val componentContext : ComponentContext
) : ComponentContext by componentContext {

    private val mapComponent = MapComponent(childContext("mapComponent"))

    private val getMapInterface : GetMapInterface = {
        mapComponent.createInterface(it)
    }
}

typealias GetMapInterface = (lifecycle : Lifecycle) -> MapInterface
