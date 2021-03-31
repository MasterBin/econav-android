package ru.nk.econav.mapscreen

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.nk.econav.mapscreen.MainMapStore.*
import ru.nk.econav.mapscreen.data.LatLon
import ru.nk.econav.mapscreen.data.PathRepository

class MainMapStoreProvider(
    private val storeFactory: StoreFactory,
    private val pathRepository : PathRepository
) {
    fun create(): MainMapStore =
        object : MainMapStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainMapStore",
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = State()
        ) {}


    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            is Intent.SetStartPoint -> dispatch(Result.StartPointSet(intent.start))
            is Intent.SetEndPoint -> {
                dispatch(Result.EndPointSet(intent.endPoint))
                getRoute(getState)
            }
        }

        private suspend fun getRoute(getState: () -> State) {
            val state = getState()
            val res = pathRepository.getPath(state.startPoint!!, state.endPoint!!)

            dispatch(Result.PathReceived(res.encodedRoute))
        }

    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.EndPointSet -> copy(endPoint = result.endPoint)
                is Result.PathReceived -> copy(route = result.path)
                is Result.StartPointSet -> copy(startPoint = result.startPoint)
            }
    }


    sealed class Result {
        data class StartPointSet(val startPoint : LatLon) : Result()
        data class EndPointSet(val endPoint : LatLon) : Result()
        data class PathReceived(val path : String) : Result()
    }

}