package ru.nk.econav.android.routing.impl

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.routing.api.RouteReq
import ru.nk.econav.android.routing.impl.RoutingStore.*

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
            is Intent.ChangeEcoParam -> changeEcoParam(
                ecoParam = intent.ecoParam,
                getState = getState
            )
            is Intent.RouteRequest -> requestRoute(intent.req, getState().ecoParam)
        }

        private suspend fun changeEcoParam(ecoParam: Float, getState: () -> State) {
            dispatch(Result.ChangeEcoParam(ecoParam))
            val state = getState().routingState
            if (state is State.Routing.RouteCreated) {
                requestRoute(state.routeReq, getState().ecoParam)
            }
        }

        private val requestScope = MainScope()

        private suspend fun requestRoute(routeReq: RouteReq, ecoParam: Float) {
            requestScope.coroutineContext.cancelChildren()

            requestScope.launch {
                val route = routingRepository.getPath(routeReq.start, routeReq.end, ecoParam)
                dispatch(Result.ShowRoute(routeReq, route))
            }
        }
    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                Result.Cancel -> copy(routingState = State.Routing.General)
                is Result.ShowRoute -> copy(
                    routingState = State.Routing.RouteCreated(
                        result.route,
                        result.routeReq
                    )
                )
                is Result.ChangeEcoParam -> copy(ecoParam = result.ecoParam)
            }
    }

    private sealed class Result {
        object Cancel : Result()
        data class ChangeEcoParam(val ecoParam: Float) : Result()
        data class ShowRoute(val routeReq: RouteReq, val route: Route) : Result()
    }

}