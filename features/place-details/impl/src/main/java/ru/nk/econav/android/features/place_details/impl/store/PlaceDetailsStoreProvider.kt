package ru.nk.econav.android.features.place_details.impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import ru.nk.econav.android.features.place_details.impl.store.PlaceDetailsStore.*

class PlaceDetailsStoreProvider(
    private val storeFactory: StoreFactory
) {
    fun create(): PlaceDetailsStore =
        object : PlaceDetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "PlaceDetailsStore",
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl(),
            initialState = State()
        ) {}


    private inner class ExecutorImpl : SuspendExecutor<Intent, Nothing, State, Result, Label>() {

        override suspend fun executeIntent(intent: Intent, getState: () -> State) {}// when (intent) {

        //}

    }

    private class ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(res: Result): State = State()
//            when (res) {
//
//            }
    }


    sealed class Result {

    }

}