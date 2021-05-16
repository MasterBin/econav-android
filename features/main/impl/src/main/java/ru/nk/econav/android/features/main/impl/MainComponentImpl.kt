package ru.nk.econav.android.features.main.impl

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.push
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.parcelize.Parcelize
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.store.MainStoreProvider
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.appRouter
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.core.common.util.getStore

@OptIn(InternalCoroutinesApi::class)
class MainComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: MainComponent.Dependencies,
    private val children: MainComponent.Children
) : MainComponent, AppComponentContext by appComponentContext, MainComponent.Dependencies by deps {

    private val store = instanceKeeper.getStore {
        MainStoreProvider(LoggingStoreFactory(DefaultStoreFactory))
            .create()
    }

    sealed class Child : Parcelable {

        @Parcelize
        object General : Child()

        @Parcelize
        data class PlaceDetails(val place: GeoFeature) : Child()
    }

    private val router = appRouter<Child, Any>(
        initialConfiguration = Child.General,
        handleBackButton = true,
        key = "mainRouter",
        childFactory = ::createChildren
    )

    val routerState = router.state

    private fun createChildren(
        config: Child,
        appComponentContext: AppComponentContext
    ): Any =
        when (config) {
            Child.General -> {
                children.general.invoke(
                    appComponentContext,
                    deps = object : MainComponent.Dependencies by deps,
                        GeneralComponent.Dependencies {
                        override val placeSelected: OutEvent<GeoFeature> = OutEvent {
                            router.push(Child.PlaceDetails(it))
                        }
                    }
                )
            }
            is Child.PlaceDetails -> {
                children.placeDetails.invoke(appComponentContext,
                    deps = object : PlaceDetailsComponent.Dependencies,
                        MainComponent.Dependencies by deps {
                        override val place: GeoFeature = config.place
                    })
            }
        }

    override fun render(modifier: Modifier): Content = {
        MainComponentView(modifier = modifier, component = this)
    }
}

@Composable
fun MainComponentView(
    modifier: Modifier = Modifier,
    component: MainComponentImpl
) {
    Box(Modifier.fillMaxSize()) {
        Children(routerState = component.routerState) {
            when(it.configuration) {
                MainComponentImpl.Child.General -> {
                    (it.instance as GeneralComponent).render(modifier).invoke()
                }
                is MainComponentImpl.Child.PlaceDetails -> {
                    (it.instance as PlaceDetailsComponent).render(modifier).invoke()
                }
            }
        }
    }
}