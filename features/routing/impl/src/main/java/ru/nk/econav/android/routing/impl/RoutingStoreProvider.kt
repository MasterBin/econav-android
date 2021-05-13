package ru.nk.econav.android.routing.impl

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.*
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.routing.impl.RoutingStore.*
import ru.nk.econav.core.common.models.LatLon

class RoutingStoreProvider(
    private val storeFactory: StoreFactory,
    private val routingRepository: RoutingRepository
) {

    fun create(): RoutingStore =
        object : RoutingStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RoutingStore",
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = State()
        ) {}


    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.Cancel -> dispatch(Result.Cancel)
            Intent.CreateRouteByPlacingPoints -> dispatch(Result.StartPlacingPoints)
            is Intent.PlacePoint -> placingPoints(intent.point, getState)
            is Intent.ChangeEcoParam -> changeEcoParam(ecoParam = intent.ecoParam, getState = getState)
        }

        private suspend fun changeEcoParam(ecoParam: Float, getState: () -> State) {
            dispatch(Result.ChangeEcoParam(ecoParam))
            val state = getState().routingState
            if (state is State.Routing.RouteCreated) {
                requestRoute(state.route.from, state.route.to, getState().ecoParam)
            }
        }

        private suspend fun placingPoints(point: LatLon, getState: () -> State) {
            val state = getState().routingState
            if (state !is State.Routing.CreateRouteByPlacingPoints) return

            when {
                state.startPoint == null -> {
                    dispatch(Result.PlacingPoints(state.copy(startPoint = point)))
                }
                state.endPoint == null -> {
                    dispatch(Result.PlacingPoints(state.copy(endPoint = point)))
                    requestRoute(state.startPoint, point, getState().ecoParam)
                }
            }
        }

        private var requestScope = MainScope()

        private suspend fun requestRoute(start : LatLon, end : LatLon, ecoParam: Float) {
            requestScope.cancel()
            requestScope = MainScope()
            requestScope.launch {
                val route = routingRepository.getPath(start, end, ecoParam)
                dispatch(Result.ShowRoute(route))
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
                is Result.ChangeEcoParam -> copy(ecoParam = result.ecoParam)
            }
    }

    private sealed class Result {
        object Cancel : Result()
        data class PlacingPoints(val routingState: State.Routing.CreateRouteByPlacingPoints) :
            Result()

        data class ChangeEcoParam(val ecoParam : Float) : Result()
        object StartPlacingPoints : Result()
        data class ShowRoute(val route: Route) : Result()
    }

}