package ru.nk.econav.android.map

import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.statekeeper.StateKeeper
import com.arkivanov.decompose.value.Value
import ru.nk.econav.android.RootContainer

interface MainMap {

    val model : Model

    interface Model {
        val data : Value<Data>

    }

    class Data(
        val text : String
    )
}