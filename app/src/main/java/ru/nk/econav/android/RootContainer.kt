package ru.nk.econav.android

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.consume
import com.arkivanov.decompose.value.Value
import kotlinx.android.parcel.Parcelize
import org.koin.java.KoinJavaComponent
import ru.nk.econav.android.RootContainer.*
import ru.nk.econav.android.map.MainMap
import ru.nk.econav.android.map.PathRepository
import ru.nk.econav.extended_lifecycle.LifecycleExtension

interface RootContainer {

    val routerState : Value<RouterState<*, Child>>

    interface Events {
        fun intro()
        fun mainMap()
    }

    sealed class Child {
        data class Map(val component : MainMap) : Child()
    }

    interface Dependencies {
        val pathRepository : PathRepository
        val lifecycleExtension : LifecycleExtension
    }
}

@Suppress("FunctionName")
fun RootContainer(componentContext: ComponentContext, dependencies: Dependencies): RootContainer =
    RootContainerImpl(componentContext, dependencies)