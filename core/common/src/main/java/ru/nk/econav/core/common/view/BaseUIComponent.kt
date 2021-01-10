package ru.nk.econav.core.common.view

import android.view.LayoutInflater
import android.view.View
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel

@ExperimentalCoroutinesApi
abstract class BaseUIComponent<Model : Any, Event : Any> : UIComponent {

    private val componentScope = CoroutineScope(Dispatchers.Main)
    private val eventsChannel = BroadcastChannel<Event>(1)

    abstract val renderer : Renderer<Model>

    protected fun event(event : Event) {
        componentScope.launch {
            eventsChannel.send(event)
        }
    }
}