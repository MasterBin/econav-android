package ru.nk.econav.android.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import ru.nk.econav.core.common.decopmose.Content

class RootComponent(
    private val componentContext : ComponentContext
) : ComponentContext by componentContext {

//    val routerState: Value<RouterState<*, Any>>
//        get() = router.state

//    private val router = router<Config, Content>(
//        initialConfiguration = Config.Main,
//        childFactory = ::createChild,
//        handleBackButton = true
//    )
//
//    private fun createChild(config : Config, componentContext : ComponentContext) = when(config) {
//        Config.Main ->
//
//        )
//    }

    private sealed class Config : Parcelable {
        @Parcelize
        object Main : Config()
    }

}