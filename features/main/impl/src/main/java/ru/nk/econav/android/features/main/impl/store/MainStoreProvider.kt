package ru.nk.econav.android.features.main.impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.nk.econav.android.features.main.impl.store.MainStore.*

class MainStoreProvider(
    private val storeFactory: StoreFactory
) {

    fun create(): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            reducer = ReducerImpl(),
            executorFactory = ::ExecutorImpl,
            initialState = State()
        ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.Cancel -> dispatch(Result.Cancel)
            Intent.CreateRouteByPlacingPoints -> dispatch(Result.StartPlacingPoints)
            is Intent.ChangeEcoParam -> dispatch(Result.ChangeEcoParam(intent.ecoParam))
            is Intent.RouteDetailsReceived -> dispatch(Result.Route(intent.distance, intent.time))
        }
    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                Result.Cancel -> copy(routingState = State.Routing.General)
                Result.StartPlacingPoints -> copy(routingState = State.Routing.PlacingPoints)
                is Result.ChangeEcoParam -> copy(ecoParam = result.ecoParam)
                is Result.Route -> copy(routingState = State.Routing.Route(result.distance, result.time))
            }
    }

    private sealed class Result {
        object Cancel : Result()
        data class ChangeEcoParam(val ecoParam : Float) : Result()
        data class Route(val distance : String, val time : String) : Result()

        object StartPlacingPoints : Result()
    }
}