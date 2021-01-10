package ru.nk.econav.android

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.consume
import com.arkivanov.decompose.value.Value
import kotlinx.android.parcel.Parcelize
import ru.nk.econav.android.RootContainer.*
import ru.nk.econav.android.map.MainMap

fun RootContainer(componentContext: ComponentContext): RootContainer =
    RootContainerImpl(componentContext)

interface RootContainer {

    val model: Model

    interface Model : Events {
        val child: Value<RouterState<Config, Any>>
    }

    interface Events {
        fun intro()
        fun mainMap()
    }

    enum class Screens {
        INTRO,
        MAINMAP
    }

    @Parcelize
    data class Config(val screen : Screens) : Parcelable
}

class RootContainerImpl(private val componentContext: ComponentContext) : RootContainer,
    Events,
    ComponentContext by componentContext {

    init {
        stateKeeper.register("a") { Config(Screens.MAINMAP) }
    }

    private val router: Router<Config, Any> =
        router(
            initialConfiguration = stateKeeper.consume<Config>("a") ?: Config(Screens.MAINMAP),
            componentFactory = ::resolveChild
        )

    override val model: Model =
        object : Model, Events by this {
            override val child: Value<RouterState<Config, Any>> = router.state
        }

    private fun resolveChild(config : Config, componentContext: ComponentContext) : Any {
        return when(config.screen) {
            Screens.INTRO -> TODO()
            Screens.MAINMAP -> MainMap(componentContext)
        }
    }

    override fun intro() {
        router.pop()
        router.push(Config(Screens.INTRO))
    }

    override fun mainMap() {
        router.pop()
        router.push(Config(Screens.MAINMAP))
    }
}