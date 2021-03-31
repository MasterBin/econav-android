package ru.nk.econav.mapscreen

import com.arkivanov.mvikotlin.core.store.Store
import ru.nk.econav.mapscreen.ExampleStore.*

interface ExampleStore : Store<Intent, State, Label> {

    sealed class Intent {

    }

    data class State(
        val example: String
    )

    sealed class Label {

    }
}