package ru.nk.econav.android.features.main.impl

import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.replaceCurrent
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.navigation.api.NavigationComponent
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.appRouter
import ru.nk.econav.core.common.util.OutEvent

@OptIn(InternalCoroutinesApi::class)
class MainComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: MainComponent.Dependencies,
    private val children: MainComponent.Children
) : MainComponent, AppComponentContext by appComponentContext, MainComponent.Dependencies by deps {

    sealed class Child : Parcelable {
        @Parcelize
        object General : Child()

        @Parcelize
        data class PlaceDetails(val place: GeoFeature) : Child()

        @Parcelize
        data class Navigation(val route: Route) : Child()
    }

    private val router = appRouter<Child, Any>(
        initialConfiguration = Child.General,
        handleBackButton = true,
        key = "mainRouter",
        childFactory = ::createChildren
    )

    val routerState = router.state

    init {
        handleBackButtonWhenStackIsEmpty()
    }

    var doubleBackToExit = false

    private fun handleBackButtonWhenStackIsEmpty() {
        backPressedDispatcher.register {
            if (doubleBackToExit || router.state.value.backStack.isNotEmpty()) {
                false
            } else {
                doubleBackToExit = true
                Toast.makeText(
                    applicationContext,
                    applicationContext.resources.getString(R.string.back_button_exit_text),
                    Toast.LENGTH_SHORT
                ).show()
                componentScope.launch {
                    withContext(Dispatchers.Default) {
                        delay(2000)
                        withContext(Dispatchers.Main) {
                            doubleBackToExit = false
                        }
                    }
                }
                true
            }
        }
    }

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
                        override val navigateTo: OutEvent<Route> = OutEvent {
                            router.replaceCurrent(Child.Navigation(it))
                        }
                    })
            }
            is Child.Navigation -> {
                children.navigation.invoke(appComponentContext,
                    deps = object : NavigationComponent.Dependencies,
                        MainComponent.Dependencies by this {
                        override val route: Route = config.route
                        override val permissionNotGranted: OutEvent<Unit> = OutEvent {
                            //TODO:
                        }
                    }
                )
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
            val innerModifier = modifier
                .navigationBarsPadding(bottom = true)
                .align(Alignment.TopCenter)
            when (it.configuration) {
                MainComponentImpl.Child.General -> {
                    (it.instance as GeneralComponent).render(innerModifier).invoke()
                    NavigationBarBackground()
                }
                is MainComponentImpl.Child.PlaceDetails -> {
                    (it.instance as PlaceDetailsComponent).render(innerModifier).invoke()
                    NavigationBarBackground()
                }
                is MainComponentImpl.Child.Navigation -> {
                    (it.instance as NavigationComponent).render(innerModifier).invoke()
                }
            }
        }
    }
}

@Composable
fun BoxScope.NavigationBarBackground() {
    Box(
        modifier = Modifier
            .navigationBarsHeight()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(color = MaterialTheme.colors.surface)
    )
}