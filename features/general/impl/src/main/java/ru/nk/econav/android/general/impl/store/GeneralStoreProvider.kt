package ru.nk.econav.android.general.impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.general.impl.store.GeneralStore.*
import ru.nk.econav.core.common.models.LatLon

class GeneralStoreProvider(
    private val storeFactory: StoreFactory
) {
    fun create(): GeneralStore =
        object : GeneralStore, Store<Intent, State, Label> by storeFactory.create(
            name = "GeneralStore",
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = State()
        ) {}


    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.PermissionNotGranted -> dispatch(Result.PermissionNotGranted)
            is Intent.SetUserLocation -> dispatch(Result.SetUserLocation(intent.location))
            is Intent.SearchTextChanged -> dispatch(Result.SearchTextChanged(intent.text))
            is Intent.SelectedPlace -> selectedPlace(intent.place, getState)
            is Intent.SelectedStartLocation -> dispatch(Result.SelectedStartLocation(intent.location))
            Intent.ShowUserLocationButton -> dispatch(Result.ShowUserLocationButton)
        }

        private fun selectedPlace(place : GeoFeature, getState: () -> State) {
            val state = getState()
            if (state.startLocation != null) {
                publish(Label.RouteToPlace(state.startLocation, place, state.isUserLocation))
            } else {
                publish(Label.NoStartLocation)
            }
        }

    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(res: Result): State = when(res) {
            is Result.SearchTextChanged -> copy(
                searchText = res.text
            )
            is Result.SelectedStartLocation -> copy(
                startLocation = res.location,
                isUserLocation = false
            )
            is Result.SetUserLocation -> copy(
                isUserLocation = true,
                startLocation = res.location,
                showUserLocationButton = true
            )
            Result.PermissionNotGranted -> copy(
                showUserLocationButton = false
            )
            Result.ShowUserLocationButton -> copy(
                showUserLocationButton = true
            )
        }
    }


    sealed class Result {
        object PermissionNotGranted : Result()
        object ShowUserLocationButton : Result()
        data class SetUserLocation(val location: LatLon) : Result()
        data class SelectedStartLocation(val location: LatLon) : Result()
        data class SearchTextChanged(val text: String) : Result()
    }

}