package ru.nk.econav.android.features.main.impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.nk.econav.android.features.main.impl.store.MainStore.*
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.core.common.models.LatLon

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
            is Intent.PlacePoint -> placingPoints(intent.point, getState)
            is Intent.RouteReceived -> dispatch(Result.ShowRoute(intent.route))
        }

        private fun placingPoints(point: LatLon, getState: () -> State) {
            val state = getState().routingState
            if (state !is State.Routing.CreateRouteByPlacingPoints) return

            when {
                state.startPoint == null -> {
                    dispatch(Result.PlacingPoints(state.copy(startPoint = point)))
                }
                state.endPoint == null -> {
                    dispatch(Result.PlacingPoints(state.copy(endPoint = point)))
                    publish(Label.RequestRoute(state.startPoint, point))
                }
            }
        }

    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                Result.Cancel -> copy(routingState = State.Routing.General)
                is Result.ShowRoute -> copy(routingState = State.Routing.RouteCreated(result.route))
                Result.StartPlacingPoints -> copy(routingState = State.Routing.CreateRouteByPlacingPoints())
                is Result.PlacingPoints -> copy(routingState = result.routingState)
            }
    }

    private sealed class Result {
        object Cancel : Result()
        data class PlacingPoints(val routingState: State.Routing.CreateRouteByPlacingPoints) :
            Result()

        object StartPlacingPoints : Result()
        data class ShowRoute(val route: Route) : Result()
    }
}