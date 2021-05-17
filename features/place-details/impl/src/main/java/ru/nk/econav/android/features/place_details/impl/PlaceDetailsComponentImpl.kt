package ru.nk.econav.android.features.place_details.impl

import androidx.compose.ui.Modifier
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.routing.models.Route
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.routing.api.RouteReq
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

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

    private val ecoParamFlow = MutableStateFlow(0f)
    private val requestRoute = MutableSharedFlow<RouteReq>()

    private val _state = MutableValue(State())
    val model: Value<State> = _state

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

    private val mapInterface = getMapInterface(lifecycle)

    fun setMapOffset(offset: Int) {
        mapInterface.setMapCenterOffset(
            0,
            (-offset / applicationContext.resources.displayMetrics.density).toInt()
        )
    }

    fun displayRouteDetails(route: Route) {
        _state.reduce {
            State(
                route
            )
        }
    }

    fun routeToClicked() {
        model.value.route?.let { navigateTo.invoke(it) }
    }

    data class State(
        val route: Route? = null
    )
}

