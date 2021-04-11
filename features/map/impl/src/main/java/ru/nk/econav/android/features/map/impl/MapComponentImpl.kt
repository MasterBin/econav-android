package ru.nk.econav.android.features.map.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.android.features.map.impl.map.InnerMapComponent
import ru.nk.econav.android.features.map.impl.map.OsmMap
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.decopmose.Content
import ru.nk.econav.core.common.decopmose.appRouter
import ru.nk.econav.core.common.decopmose.childContext

class MapComponentImpl(
    private val componentContext: AppComponentContext,
    private val children: MapComponent.Children
) : MapComponent, AppComponentContext by componentContext {

    val innerMapComponent = InnerMapComponent(childContext("mapComponent"))

    private val getMapInterface: GetMapInterface = {
        innerMapComponent.createInterface(it)
    }

    val mainComponent by lazy {
        children.mainComponent.invoke(
            childContext("main"),
            object : MainComponent.Dependencies {
                override val getMapInterface: GetMapInterface =
                    this@MapComponentImpl.getMapInterface
            }
        )
    }

//    val appRouter = appRouter<Config, Any>(
//        initialConfiguration = Config(""),
//        childFactory = { conf, c ->
//            InnerMapComponent(c)
//        }
//    )

    @Parcelize
    data class Config(val s : String) : Parcelable

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
        component.mainComponent
            .render(Modifier.fillMaxSize())
            .invoke()
    }
}


