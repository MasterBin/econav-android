package ru.nk.econav.android.map

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.google.android.gms.maps.model.LatLng
import ru.nk.econav.extended_lifecycle.LifecycleExtension

interface MainMap {

    val model : Model

    val lifecycleExtension : LifecycleExtension

    interface Model : Events {
        val data : Value<Data>
    }

    interface Events {
        fun setStartPoint(latLng: LatLng)
        fun setEndPoint(latLng: LatLng)
        fun clearAll()
    }

    data class Data(
        val startPoint : LatLng ? = null,
        val endPoint : LatLng ? = null,
        val state : State = State.None
    )

    interface Dependencies {
        val pathRepository : PathRepository
        val lifecycleExtension : LifecycleExtension
    }

    sealed class State {
        object None : State()
        object Some : State()
        object LoadingPath : State()
        data class PathLoaded(val formattedPath : String) : State()
    }
}

fun MainMap(componentContext: ComponentContext, dependencies : MainMap.Dependencies) : MainMap =
    MainMapImpl(componentContext, dependencies)