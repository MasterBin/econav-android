package ru.nk.econav.android.features.map.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.android.features.map.impl.map.InnerMapComponent
import ru.nk.econav.android.features.map.impl.map.OsmMap
import ru.nk.econav.core.common.decompose.*

class MapComponentImpl(
    private val componentContext: AppComponentContext,
    private val children: MapComponent.Children
) : MapComponent, AppComponentContext by componentContext {

    val innerMapComponent = InnerMapComponent(childContext("mapComponent"))

    private val getMapInterface: GetMapInterface = {
        innerMapComponent.createInterface(it)
    }

    val mainComponentChild = oneChild {
        children.mainComponent.invoke(
            it,
            object : MainComponent.Dependencies {
                override val getMapInterface: GetMapInterface =
                    this@MapComponentImpl.getMapInterface
            }
        )
    }

    override fun render(modifier: Modifier): Content = {
        MapCompose(modifier = modifier, component = this)
    }
}


@Composable
fun MapCompose(
    modifier : Modifier,
    component: MapComponentImpl
) {
    Box(modifier = modifier.fillMaxSize()) {
        OsmMap(
            modifier.fillMaxSize(),
            component = component.innerMapComponent
        )
        OneChild(component.mainComponentChild.state) {
            it.instance.render(Modifier.fillMaxSize()).invoke()
        }
    }
}


