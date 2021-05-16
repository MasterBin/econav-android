package ru.nk.econav.android.routing.impl

import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.routing.impl.util.toGeoPoint
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.util.getStore

class RoutingComponentImpl(
    private val componentContext: AppComponentContext,
    private val deps: RoutingComponent.Dependencies,
    private val routingRepository: RoutingRepository
) : RoutingComponent, RoutingComponent.Dependencies by deps,
    AppComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        RoutingStoreProvider(
            LoggingStoreFactory(DefaultStoreFactory),
            routingRepository
        ).create()
    }

    private val mapInterface = getMapInterface.invoke(lifecycle)

    private val overlayPoints = ItemizedIconOverlay<OverlayItem>(
        applicationContext,
        mutableListOf(),
        object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return false
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }
    )

    private val polyline = Polyline()

    init {
        mapInterface.add(polyline)
        mapInterface.add(overlayPoints)
        initStore()
    }

    private fun initStore() {
        componentScope.launch {
            store.states.collect { gstate ->
                overlayPoints.removeAllItems()
                polyline.setPoints(listOf())
                when (val state = gstate.routingState) {
                    is RoutingStore.State.Routing.RouteCreated -> {
                        routeReceived.invoke(state.route)
                        state.route.also {
                            if (!state.routeReq.isStartFromUserLocation) {
                                overlayPoints.addItem(
                                    OverlayItem(
                                        null,
                                        null,
                                        it.from.toGeoPoint()
                                    ).apply {
                                        setMarker(applicationContext.getDrawable(R.drawable.ic_point_start))
                                    }
                                )
                            }

                            overlayPoints.addItem(
                                OverlayItem(
                                    null,
                                    null,
                                    it.to.toGeoPoint()
                                ).apply {
                                    setMarker(applicationContext.getDrawable(R.drawable.ic_point_end))
                                })

                            polyline.setPoints(it.polyline.map { l -> l.toGeoPoint() })
                            mapInterface.zoomToBoundingBox(
                                polyline.bounds.clone().increaseByScale(2.3f), true
                            )
                        }
                    }
                }
                mapInterface.invalidateMap()
            }
        }
        initEcoParamFlow()
        initRouteRequest()
    }

    private fun initEcoParamFlow() {
        componentScope.launch {
            ecoParam.collect {
                if (ecoParamRange.contains(it)) {
                    store.accept(RoutingStore.Intent.ChangeEcoParam(it))
                }
            }
        }
    }

    private fun initRouteRequest() {
        componentScope.launch {
            routeRequest.collect {
                store.accept(RoutingStore.Intent.RouteRequest(it))
            }
        }
    }


}