package ru.nk.econav.android.map

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.consume
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.decompose.value.reduce
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.context.KoinContext
import org.koin.java.KoinJavaComponent.inject
import ru.nk.econav.android.RootContainer
import ru.nk.econav.android.map.MainMap.Data
import ru.nk.econav.android.map.MainMap.Model



internal class MainMapImpl(
    componentContext : ComponentContext,
    dependencies: MainMap.Dependencies
) : MainMap, ComponentContext by componentContext, MainMap.Dependencies by dependencies {

    private val handler = instanceKeeper.getOrCreate("Handler") {
        Handler(MainMap.Data())
    }

    override val model: Model =
        object : Model, MainMap.Events by handler{
            override val data: Value<Data> = handler.state
        }

    inner class Handler(initialState : MainMap.Data) : InstanceKeeper.Instance, MainMap.Events {

        val state = MutableValue(initialState)
        private val scopes = CoroutineScope(Dispatchers.Main)

        override fun onDestroy() {
            scopes.cancel()
        }

        override fun setStartPoint(latLng: LatLng) {
            state.reduce {
                it.copy(
                    startPoint = latLng,
                    state = MainMap.State.Some
                )
            }
        }

        override fun setEndPoint(latLng: LatLng) {
            if (state.value.state is MainMap.State.Some) {
                state.reduce {
                    it.copy(
                        endPoint = latLng,
                        state = MainMap.State.LoadingPath
                    )
                }

                scopes.launch {
                    pathRepository.getPath(state.value.startPoint!!, state.value.endPoint!!)
                        .collect { res ->
                            state.reduce {
                                it.copy(
                                    state = MainMap.State.PathLoaded(res)
                                )
                            }
                        }
                }
            }
        }

        override fun clearAll() {
            state.reduce {
                it.copy(
                    startPoint = null,
                    endPoint = null,
                    state = MainMap.State.None
                )
            }
        }
    }
}