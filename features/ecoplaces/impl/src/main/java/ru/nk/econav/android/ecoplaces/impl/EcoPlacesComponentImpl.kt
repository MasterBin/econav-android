package ru.nk.econav.android.ecoplaces.impl

import android.util.Log
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import ru.nk.econav.android.core.mapinterface.BoundingBoxUpdate
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.ecoplaces.api.EcoPlacesComponent
import ru.nk.econav.android.ecoplaces.impl.data.Api
import ru.nk.econav.android.ecoplaces.impl.data.EcoPlace
import ru.nk.econav.android.ecoplaces.impl.data.EcoPlacesRepositoryImpl
import ru.nk.econav.core.common.decopmose.AppComponentContext

class EcoPlacesComponentImpl(
    private val componentContext: AppComponentContext,
    private val deps: EcoPlacesComponent.Dependencies,
    private val networking: Networking
) : EcoPlacesComponent, AppComponentContext by componentContext {

    val mapInterface = deps.getMapInterface(lifecycle)

    private val executor = instanceKeeper.getOrCreate { Executor(networking) }

    val pointsOverlay = ItemizedIconOverlay<OverlayItem>(
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

    init {
        mapInterface.add(pointsOverlay)

        componentScope.launch {
            mapInterface.boundingBoxUpdateFlow.collect {
                onBoxUpdate(it)
            }
        }
        componentScope.launch {
            executor.flow.collect { lst ->
                pointsOverlay.removeAllItems()
                pointsOverlay.addItems(lst.map {
                    OverlayItem(null, null, null, GeoPoint(it.location.lat, it.location.lon))
                })
                mapInterface.invalidateMap()
            }
        }
    }

    private fun onBoxUpdate(update: BoundingBoxUpdate) {
        Log.i("ASDASD", "HELLO")
        executor.requestPoints(update.boundingBox, update.fromZoom)
    }
}

private class Executor(
    networking: Networking
) : InstanceKeeper.Instance {

    private val scope = MainScope()
    private val repository = EcoPlacesRepositoryImpl(networking.createApi(Api::class.java))

    private val _flow = MutableStateFlow<List<EcoPlace>>(listOf())
    val flow: Flow<List<EcoPlace>> = _flow
    var lastBounds: BoundingBox? = null

    fun requestPoints(bounds: BoundingBox, fromZoom: Boolean) {
        if (lastBounds == null) {
            makeRequest(bounds)
            return
        }

        if (fromZoom || !inBounds(bounds = bounds, last = lastBounds!!)) {
            makeRequest(calculateNewBounds(bounds))
        }
    }

    private fun makeRequest(bounds : BoundingBox) {
        scope.coroutineContext.cancelChildren()
        scope.launch {
            val res = repository.getPlaces(bounds)
            lastBounds = bounds
            _flow.emit(res)
        }
    }

    private fun inBounds(bounds: BoundingBox, last: BoundingBox): Boolean {
        return bounds.latNorth < last.latNorth &&
                bounds.latSouth > last.latSouth &&
                bounds.lonEast < last.lonEast &&
                bounds.lonWest > last.lonWest
    }

    private fun calculateNewBounds(bounds: BoundingBox): BoundingBox {
        return BoundingBox(
            bounds.latNorth + bounds.latitudeSpan / 2,
            bounds.lonEast + bounds.longitudeSpanWithDateLine / 2,
            bounds.latSouth - bounds.latitudeSpan / 2,
            bounds.lonWest - bounds.longitudeSpanWithDateLine / 2
        ).also {
            Log.i("ASDASD", it.toString())
        }
    }

    override fun onDestroy() {
        scope.cancel()
    }

}