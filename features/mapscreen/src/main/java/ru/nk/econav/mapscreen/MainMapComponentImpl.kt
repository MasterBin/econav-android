package ru.nk.econav.mapscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import ru.nk.econav.core.common.util.asValue
import ru.nk.econav.core.common.util.getStore
import ru.nk.econav.mapscreen.MainMapComponent.Dependencies
import ru.nk.econav.mapscreen.MainMapComponent.Model
import ru.nk.econav.mapscreen.MainMapStore.Intent
import ru.nk.econav.mapscreen.data.LatLon

class MainMapComponentImpl(
    private val componentContext: ComponentContext,
    private val deps: Dependencies
) : ComponentContext by componentContext, Dependencies by deps, MainMapComponent {

    private val store = instanceKeeper.getStore {
        MainMapStoreProvider(
            storeFactory = storeFactory,
            pathRepository = pathRepository
        ).create()
    }

    override val model: Value<Model> = store.asValue().map { it.toModel() }

    override val events: MainMapComponent.UiEvents = object : MainMapComponent.UiEvents {
        override fun setStartPoint(latLng: LatLon) {
            store.accept(Intent.SetStartPoint(latLng))
        }

        override fun setEndPoint(latLng: LatLon) {
            store.accept(Intent.SetEndPoint(latLng))
        }
    }

    fun MainMapStore.State.toModel(): Model {
        return when {
            route != null -> {
                Model.PathDrawn(
                    path = route
                )
            }
            else -> {
                Model.SelectPoints(
                    from = startPoint,
                    to = endPoint
                )
            }
        }
    }

}
