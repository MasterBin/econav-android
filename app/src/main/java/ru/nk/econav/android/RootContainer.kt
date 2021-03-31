package ru.nk.econav.android

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import ru.nk.econav.android.RootContainer.*
import ru.nk.econav.extended_lifecycle.LifecycleExtension

interface RootContainer {

    val routerState : Value<RouterState<*, Child>>

    interface Events {
        fun intro()
        fun mainMap()
    }

    sealed class Child {
    }

    interface Dependencies {
        val lifecycleExtension : LifecycleExtension
    }
}

//@Suppress("FunctionName")
//fun RootContainer(componentContext: ComponentContext, dependencies: Dependencies): RootContainer =
//    RootContainerImpl(componentContext, dependencies)