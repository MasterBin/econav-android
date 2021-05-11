package ru.nk.econav.android.features.main.impl

import android.location.Location
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.store.MainStore
import ru.nk.econav.android.features.main.impl.store.MainStoreProvider
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.*
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.core.common.util.asValue
import ru.nk.econav.core.common.util.getStore
import ru.nk.econav.ui.components.DraggableBottomDrawer

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

    private val ecoParameterRange = 0f..1f

    private val routingComponent = children.routing.invoke(
        childContext("routing"),
        object : RoutingComponent.Dependencies {
            override val getMapInterface: GetMapInterface = deps.getMapInterface
            override val ecoParam: Flow<Float> = store.states.distinctUntilChanged { old, new ->
                old.ecoParam == new.ecoParam
            }.map { it.ecoParam }

            override val ecoParamRange: ClosedFloatingPointRange<Float> = ecoParameterRange
            override val routeReceived: OutEvent<Route> = OutEvent {
                store.accept(MainStore.Intent.RouteDetailsReceived(it.distance, it.time))
            }
        }
    )

    val userLocationComponentChild = oneChild("2") {
        children.userLocation.invoke(
            it,
            object : UserLocationComponent.Dependencies {
                override val getMapInterface: GetMapInterface = deps.getMapInterface
                override val userLocation: OutEvent<Location> = OutEvent {
                    Log.i("LocationASDASD", "${it.altitude}${it.longitude}")
                }
            }
        )
    }


//    private val ecoPlacesComponent = children.ecoPlaces.invoke(
//        childContext("ecoPlaces"),
//        object : EcoPlacesComponent.Dependencies {
//            override val getMapInterface: GetMapInterface = deps.getMapInterface
//        }
//    )


    val ecoParamElector = oneChild("1") {
        children.ecoParamElector.invoke(
            childContext("ecoParamElector"),
            object : EcoParamElector.Dependencies {
                override val range: ClosedFloatingPointRange<Float> = ecoParameterRange
                override val valueChanged: OutEvent<Float> = OutEvent {
                    store.accept(MainStore.Intent.ChangeEcoParam(it))
                }
            }
        )
    }

    val model = store.asValue()

    fun setPointsClicked() {
        store.accept(MainStore.Intent.CreateRouteByPlacingPoints)
        routingComponent.startPlacingPoints()
    }

    fun clearPoints() {
        store.accept(MainStore.Intent.Cancel)
        routingComponent.clearPoints()
    }

    fun setMapOffset(offset : Int) {
        getMapInterface.invoke(lifecycle).setMapCenterOffset(0, (-offset / applicationContext.resources.displayMetrics.density).toInt() )
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
    val state by component.model.asState()

    SideEffect {

    }

    DraggableBottomDrawer(
        modifier = modifier.onSizeChanged {
            component.setMapOffset((it.height * 0.3).toInt())
        },
        drawerRatio = 0.3f,
        drawerContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                //TODO: move to ui module
                Crossfade(targetState = state.routingState) {
                    when (it) {
                        is MainStore.State.Routing.PlacingPoints -> {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    component.clearPoints()
                                }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                        MainStore.State.Routing.General -> {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    component.setPointsClicked()
                                }
                            ) {
                                Text(text = "Select points")
                            }
                        }
                        is MainStore.State.Routing.Route -> {
                            Column {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        component.clearPoints()
                                    }
                                ) {
                                    Text(text = "Cancel")
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(text = it.distance)
                                Text(text = it.time)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OneChild(state = component.ecoParamElector.state) {
                    it.instance.renderer(Modifier.fillMaxWidth()).invoke()
                }
            }
        },
        drawerContentExpanded = {

        },
        onDrawerContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)) {
                OneChild(state = component.userLocationComponentChild.state) {
                    it.instance
                        .render(
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp)
                        )
                        .invoke()
                }
            }
        }
    )
}