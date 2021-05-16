package ru.nk.econav.android.features.map.impl.map

import android.graphics.Bitmap
import com.arkivanov.decompose.lifecycle.DefaultLifecycleCallbacks
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.nk.econav.android.core.mapinterface.BoundingBoxUpdate
import ru.nk.econav.android.core.mapinterface.MapInterface
import ru.nk.econav.android.core.mapinterface.MyLocationInterface
import ru.nk.econav.core.common.decompose.AppComponentContext

class InnerMapComponent(
    private val componentContext: AppComponentContext
) : AppComponentContext by componentContext {

    init {
        lifecycle.subscribe(
            onDestroy = {
                componentScope.cancel()
            }
        )
    }

    val overlaysFlow = MutableStateFlow<List<Overlay>>(listOf())
    val invalidateMapFlow = MutableSharedFlow<Unit>()
    val boundingBoxZoom = MutableSharedFlow<Pair<BoundingBox, Boolean>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val getLocationOverlay = MutableSharedFlow<Unit>()
    val locationFlow = MutableSharedFlow<MyLocationNewOverlay>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val animateTo = MutableSharedFlow<GeoPoint>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val mapOffset = MutableSharedFlow<Pair<Int, Int>>()

    private val boundingBoxFlow = MutableSharedFlow<BoundingBoxUpdate>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun updateBoundingBox(box: BoundingBox, fromZoom: Boolean) {
        componentScope.launch {
            boundingBoxFlow.emit(
                BoundingBoxUpdate(
                    box,
                    fromZoom = fromZoom
                )
            )
        }
    }

    fun createInterface(lifecycle: Lifecycle): MapInterface {
        val interactor = createMapInterface()

        val listener = object : DefaultLifecycleCallbacks {
            override fun onDestroy() {
                componentScope.launch {
                    overlaysFlow.emit(overlaysFlow.value.minus(interactor.mutableOverlays))
                }
                lifecycle.unsubscribe(this)
            }
        }
        lifecycle.subscribe(listener)

        return interactor
    }

    private fun createMapInterface() = object : MapInterface {

        var mutableOverlays = mutableListOf<Overlay>()

        override fun add(overlay: Overlay) {
            componentScope.launch {
                mutableOverlays.add(overlay)
                overlaysFlow.emit(overlaysFlow.value.plus(overlay))
            }
        }

        override fun remove(overlay: Overlay) {
            componentScope.launch {
                mutableOverlays.remove(overlay)
                overlaysFlow.emit(overlaysFlow.value.minus(overlay))
            }
        }

        override fun invalidateMap() {
            componentScope.launch {
                invalidateMapFlow.emit(Unit)
            }
        }

        override fun zoomToBoundingBox(box: BoundingBox, animated: Boolean) {
            componentScope.launch {
                boundingBoxZoom.emit(box to animated)
            }
        }

        override fun moveToPoint(point: GeoPoint) {
            componentScope.launch {
                animateTo.emit(point)
            }
        }

        override fun getLocationInterface(): Flow<MyLocationInterface> {
            if (locationFlow.replayCache.isEmpty())
                componentScope.launch {
                    getLocationOverlay.emit(Unit)
                }

            return locationFlow
                .take(1)
                .map { createMapLocationInterface(it) }
        }

        override fun setMapCenterOffset(x: Int, y: Int) {
            componentScope.launch {
                mapOffset.emit(x to y)
            }
        }

        override val boundingBoxUpdateFlow: Flow<BoundingBoxUpdate> = boundingBoxFlow
    }

    private fun createMapLocationInterface(myLocationNewOverlay: MyLocationNewOverlay): MyLocationInterface {
        return object : MyLocationInterface {

            private var overlay: MyLocationNewOverlay? = myLocationNewOverlay

            init {
                lifecycle.subscribe(onDestroy = {
                    disableMyLocation()
                    overlay = null
                })
            }

            override fun setUserIcon(icon: Bitmap) {
                overlay?.setDirectionArrow(icon,icon)
                overlay?.setPersonHotspot(24f, 24f)
            }

            override fun setArrowIcon(personIcon: Bitmap, userIcon: Bitmap) {
                overlay?.setDirectionArrow(personIcon, userIcon)
            }

            override fun getMyLocation(): GeoPoint? {
                return overlay?.myLocation
            }

            override fun enableFollowLocation() {
                overlay?.enableMyLocation()
            }

            override fun disableFollowLocation() {
                overlay?.disableFollowLocation()
            }

            override fun isFollowLocationEnabled(): Boolean? {
                return overlay?.isFollowLocationEnabled
            }

            override fun enableMyLocation(myLocationProvider: IMyLocationProvider) {
                overlay?.enableMyLocation(myLocationProvider)
            }

            override fun disableMyLocation() {
                overlay?.disableMyLocation()
            }
        }
    }
}

//class CameraDirector {
//    fun add(cameraRule: CameraRule, componentLifecycle: Lifecycle) {
//
//    }
//
//    fun remove(cameraRule: CameraRule) {}
//
//    fun requestCameraControl(componentLifecycle: Lifecycle) {}
//}
//
//interface CameraRule {
//    var boundingLocations : List<LatLng>
//}
//
//interface MapCameraHandle {
//    val isActiveFlow : Flow<Boolean>
//    fun set(camera : )
//}

