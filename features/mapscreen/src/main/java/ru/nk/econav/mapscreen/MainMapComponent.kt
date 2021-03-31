package ru.nk.econav.mapscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.nk.econav.mapscreen.data.LatLon
import ru.nk.econav.mapscreen.data.PathRepository

interface MainMapComponent {

    val model: Value<Model>

    interface UiEvents {
        fun setStartPoint(latLng: LatLon)
        fun setEndPoint(latLng: LatLon)
    }

    val events: UiEvents

    sealed class Model {
        data class PathDrawn(
            val path: String
        ) : Model()

        data class SelectPoints(
            val from: LatLon? = null,
            val to: LatLon? = null
        ) : Model()
    }

    interface Dependencies {

        val pathRepository: PathRepository

        val storeFactory: StoreFactory
            get() = LoggingStoreFactory(DefaultStoreFactory)
    }
}

fun MainMapComponent(componentContext: ComponentContext, deps: MainMapComponent.Dependencies)
        : MainMapComponent = MainMapComponentImpl(componentContext, deps)