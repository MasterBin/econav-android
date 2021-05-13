package ru.nk.econav.android.features.searchplaces.impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.impl.store.SearchPlacesStore.*
import ru.nk.econav.core.common.models.LatLon

class SearchPlacesStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: PlacesRepository
) {

    fun create(): SearchPlacesStore =
        object : SearchPlacesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchPlacesStore",
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = State()
        ) {}


    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.SearchClicked -> search(getState, false)
            is Intent.SearchTextChanged -> {
                dispatch(Result.SearchTextChanged(intent.searchText))
                search(getState, true)
            }
            is Intent.UserLocationChanged -> dispatch(Result.UserLocationChanged(intent.location))
        }

        private var disposableScope: CoroutineScope = MainScope()

        suspend fun search(getState: () -> State, autoComplete: Boolean) {
            val state = getState()

            disposableScope.coroutineContext.cancelChildren()

            if (state.searchText.isEmpty())
                return

            disposableScope.launch {
                dispatch(
                    Result.QueryResultsReceived(
                        repository.search(
                            state.searchText,
                            autoComplete = autoComplete,
                            userLocation = state.userLocation
                        ).features
                    )
                )
            }
        }
    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(res: Result): State =
            when (res) {
                is Result.QueryResultsReceived -> copy(foundFeatures = res.res)
                is Result.UserLocationChanged -> copy(userLocation = res.userLocation)
                is Result.SearchTextChanged -> copy(searchText = res.searchText)
            }
    }


    sealed class Result {
        data class SearchTextChanged(val searchText: String) : Result()
        data class QueryResultsReceived(val res: List<GeoFeature>) : Result()
        data class UserLocationChanged(val userLocation: LatLon) : Result()
    }

}