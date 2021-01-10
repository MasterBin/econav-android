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
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.nk.econav.android.RootContainer
import ru.nk.econav.android.map.MainMap.Data
import ru.nk.econav.android.map.MainMap.Model

fun MainMap(componentContext: ComponentContext) : MainMap =
    MainMapImpl(componentContext)

internal class MainMapImpl(
    componentContext : ComponentContext,

) : MainMap, ComponentContext by componentContext {

    private val handler = instanceKeeper.getOrCreate("Handler") {
        Handler(stateKeeper.consume<State>("State") ?: State())
    }

    init {
        stateKeeper.register("State") { handler.state.value }
    }

    override val model: Model =
        object : Model {
            override val data: Value<Data> = handler.state.map { Data("$it +++") }
        }

    inner class Handler(initialState : State = State()) : InstanceKeeper.Instance {
        val state = MutableValue(initialState)
        private val scopes = CoroutineScope(Dispatchers.Main)

        init {
            scopes.launch {
                ticker(1000, 0)
                    .consumeAsFlow()
                    .flowOn(Dispatchers.Default)
                    .collect { res ->
                        state.reduce { it.copy(value = it.value + 1) }
                    }
            }
        }

        override fun onDestroy() {
            scopes.cancel()
        }
    }

    @Parcelize
    data class State(
        val value: Int = 0
    ) : Parcelable
}