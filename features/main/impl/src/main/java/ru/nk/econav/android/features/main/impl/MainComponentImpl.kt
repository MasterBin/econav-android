package ru.nk.econav.android.features.main.impl

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.ecoplaces.api.EcoPlacesComponent
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.store.MainStore
import ru.nk.econav.android.features.main.impl.store.MainStoreProvider
import ru.nk.econav.android.features.main.impl.util.toGeoPoint
import ru.nk.econav.android.features.main.impl.util.toLatLon
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.android.routing.api.model.RouteRequest
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.decopmose.Content
import ru.nk.econav.core.common.decopmose.childContext
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.core.common.util.asValue
import ru.nk.econav.core.common.util.getStore
import ru.nk.econav.core.common.util.ifNotNull
import ru.nk.econav.ui.components.DraggableBottomDrawer

@OptIn(InternalCoroutinesApi::class)
class MainComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: MainComponent.Dependencies,
    private val children: MainComponent.Children
) : MainComponent, AppComponentContext by appComponentContext, MainComponent.Dependencies by deps {

    private val mapInterface = getMapInterface.invoke(lifecycle)
    private val store = instanceKeeper.getStore {
        MainStoreProvider(LoggingStoreFactory(DefaultStoreFactory))
            .create()
    }
    private val overlayPoints = ItemizedIconOverlay<OverlayItem>(
        applicationContext,
        mutableListOf(),
        object : OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return false
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }
    )
    private val overlay = MapEventsOverlay(object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
            return false
        }

        override fun longPressHelper(p: GeoPoint): Boolean {
            placePoint(p)
            return store.state.routingState is MainStore.State.Routing.CreateRouteByPlacingPoints
        }
    })

    private val routingComponent = children.routing.invoke(
        childContext("routing"),
        object : RoutingComponent.Dependencies {
            override val route: OutEvent<Route> = OutEvent {
                store.accept(MainStore.Intent.RouteReceived(it))
            }
        }
    )

//    private val ecoPlacesComponent = children.ecoPlaces.invoke(
//        childContext("ecoPlaces"),
//        object : EcoPlacesComponent.Dependencies {
//            override val getMapInterface: GetMapInterface = deps.getMapInterface
//        }
//    )

    val ecoParamElector = children.ecoParamElector.invoke(
        childContext("ecoParamElector"),
        object : EcoParamElector.Dependencies {
            override val range: ClosedFloatingPointRange<Float> = 0f..1f
            override val valueChanged: OutEvent<Float> = OutEvent {
                store.accept(MainStore.Intent.ChangeEcoParam(it))
            }
        }
    )

    private val polyline = Polyline()

    val model = store.asValue()

    init {
        mapInterface.add(polyline)
        mapInterface.add(overlayPoints)
        mapInterface.add(overlay)

        componentScope.launch {
            store.states.collect { gstate ->
                overlayPoints.removeAllItems()
                polyline.setPoints(listOf())
                when (val state = gstate.routingState) {
                    is MainStore.State.Routing.CreateRouteByPlacingPoints -> {
                        state.startPoint.ifNotNull {
                            overlayPoints.addItem(OverlayItem(null, null, it.toGeoPoint()).apply {
                                setMarker(applicationContext.getDrawable(R.drawable.ic_point_start))
                            })
                        }
                        state.endPoint.ifNotNull {
                            overlayPoints.addItem(OverlayItem(null, null, it.toGeoPoint()).apply {
                                setMarker(applicationContext.getDrawable(R.drawable.ic_point_end))
                            })
                        }
                    }
                    is MainStore.State.Routing.RouteCreated -> {
                        state.route.also {
                            overlayPoints.addItem(
                                OverlayItem(
                                    null,
                                    null,
                                    it.from.toGeoPoint()
                                ).apply {
                                    setMarker(applicationContext.getDrawable(R.drawable.ic_point_start))
                                }
                            )
                            overlayPoints.addItem(
                                OverlayItem(
                                    null,
                                    null,
                                    it.to.toGeoPoint()
                                ).apply {
                                    setMarker(applicationContext.getDrawable(R.drawable.ic_point_end))
                                })
                            polyline.setPoints(it.polyline.map { l -> l.toGeoPoint() })
                        }
                    }
                }
                mapInterface.invalidateMap()
            }
        }
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is MainStore.Label.RequestRoute -> requestRoute(
                        it.startPoint,
                        it.endPoint,
                        it.ecoParam
                    )
                }
            }
        }
    }

    private fun requestRoute(from: LatLon, to: LatLon, ecoParam: Float) {
        routingComponent.requestRoute(
            RouteRequest(
                from,
                to,
                ecoParameter = ecoParam
            )
        )
    }

    private fun placePoint(p: GeoPoint) {
        store.accept(MainStore.Intent.PlacePoint(p.toLatLon()))
    }

    fun setPointsClicked() {
        store.accept(MainStore.Intent.CreateRouteByPlacingPoints)
    }

    fun clearPoints() {
        store.accept(MainStore.Intent.Cancel)
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

    DraggableBottomDrawer(
        modifier = modifier,
        drawerRatio = 0.3f,
        drawerContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                //TODO: move to ui module
                Crossfade(targetState = state.routingState) {
                    when (it) {
                        is MainStore.State.Routing.RouteCreated,
                        is MainStore.State.Routing.CreateRouteByPlacingPoints -> {
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
                    }
                }
                Spacer(Modifier.height(12.dp))
                component.ecoParamElector.renderer(Modifier.fillMaxWidth()).invoke()
            }
        },
        drawerContentExpanded = {

        },
        onDrawerContent = {

        }
    )
}