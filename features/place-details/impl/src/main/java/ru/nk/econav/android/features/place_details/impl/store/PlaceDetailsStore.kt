package ru.nk.econav.android.features.place_details.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.android.features.place_details.impl.store.PlaceDetailsStore.*

interface PlaceDetailsStore : Store<Intent, State, Label> {

    sealed class Intent {

    }

    data class State(
        val example: String = ""
    )

    sealed class Label {

    }
}
