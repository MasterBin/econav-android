package ru.nk.econav.android.features.place_details.impl

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.routing.api.RouteReq
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.ui.components.DraggableBottomDrawer

class PlaceDetailsComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: PlaceDetailsComponent.Dependencies,
    private val children: PlaceDetailsComponent.Children
) : PlaceDetailsComponent, AppComponentContext by appComponentContext,
    PlaceDetailsComponent.Dependencies by deps {

    override fun render(modifier: Modifier): Content = { PlaceDetails(modifier, this) }

    val userLocationComponent = oneChild("userLocation") {
        children.userLocation.invoke(
            it,
            object : UserLocationComponent.Dependencies,
                PlaceDetailsComponent.Dependencies by this {
                override val userLocation: OutEvent<LatLon> = OutEvent { location ->
                    showRoute(location)
                }
                override val permissionNotGranted: OutEvent<Unit> = OutEvent {
                    //TODO:
                }
            })
    }

    private val ecoParamFlow = MutableSharedFlow<Float>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val requestRoute = MutableSharedFlow<RouteReq>()

    private val routingComponent = oneChild("routing") {
        children.routing.invoke(
            it,
            object : RoutingComponent.Dependencies, PlaceDetailsComponent.Dependencies by this {
                override val ecoParam: Flow<Float> = ecoParamFlow
                override val ecoParamRange: ClosedFloatingPointRange<Float> = 0f..1f
                override val routeReceived: OutEvent<Route> = OutEvent { route ->
                    displayRouteDetails(route)
                }
                override val routeRequest: Flow<RouteReq> = requestRoute
            })
    }

    val ecoParamElector = oneChild("ecoParam") {
        children.ecoParamElector.invoke(it, object : EcoParamElector.Dependencies {
            override val range: ClosedFloatingPointRange<Float> = 0f..1f
            override val valueChanged: OutEvent<Float> = OutEvent {
                componentScope.launch { ecoParamFlow.emit(it) }
            }
        })
    }

    var routeRequested = false

    fun showRoute(location: LatLon) {
        componentScope.launch {
            if (!routeRequested) {
                requestRoute.emit(
                    RouteReq(
                        start = location,
                        end = deps.place.center,
                        isStartFromUserLocation = true
                    )
                )
                routeRequested = true
            }
        }
    }

    fun displayRouteDetails(route: Route) {
        //TODO
    }

}

@Composable
fun PlaceDetails(
    modifier: Modifier = Modifier,
    component: PlaceDetailsComponentImpl
) {
    DraggableBottomDrawer(
        modifier = modifier,
        drawerRatio = 0.3f,
        onDrawerContent = {
            OnDrawer(component = component)
        },
        drawerContent = {
            DrawerContent(component = component)
        }
    )
}

@Composable
fun DrawerContent(
    component: PlaceDetailsComponentImpl
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.wrapContentWidth(Alignment.Start)) {
                Text(
                    text = component.place.name,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = component.place.address,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Text(
                text = component.place.distanceTo ?: "",
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
fun OnDrawer(
    component: PlaceDetailsComponentImpl
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                OneChild(state = component.userLocationComponent.state) {
                    it.instance
                        .render(
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp)
                        )
                        .invoke()
                }
            }

            OneChild(state = component.ecoParamElector.state) {
                it.instance.renderer(Modifier.fillMaxWidth())
                    .invoke()
            }
        }
    }
}