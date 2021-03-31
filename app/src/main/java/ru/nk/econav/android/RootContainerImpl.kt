package ru.nk.econav.android

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import kotlinx.android.parcel.Parcelize
import ru.nk.econav.android.RootContainer.*

//class RootContainerImpl(
//    componentContext: ComponentContext,
//    dependencies: Dependencies
//) : RootContainer, ComponentContext by componentContext, Dependencies by dependencies {
//
//    private val router = router<Configuration, Child>(
//        initialConfiguration = Configuration.MainMap,
//        handleBackButton = true,
//        componentFactory = ::createChild
//    )
//
//    override val routerState: Value<RouterState<*, Child>> = router.state
//
//    private fun createChild(config : Configuration, componentContext: ComponentContext) : Child =
//        when(config) {
//            Configuration.MainMap -> Child.Map(createMainMap(componentContext))
//        }
//
//    private fun createMainMap(componentContext: ComponentContext) =
//        MainMap(
//            componentContext,
//            object : MainMap.Dependencies, Dependencies by this {}
//        )
//
//
//    private sealed class Configuration : Parcelable {
//        @Parcelize
//        object MainMap : Configuration()
//    }
//}
