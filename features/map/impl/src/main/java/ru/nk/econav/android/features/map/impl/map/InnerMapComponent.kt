package ru.nk.econav.android.features.map.impl.map

import android.util.Log
import com.arkivanov.decompose.lifecycle.DefaultLifecycleCallbacks
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.overlay.Overlay
import ru.nk.econav.android.core.mapinterface.BoundingBoxUpdate
import ru.nk.econav.android.core.mapinterface.MapInterface
import ru.nk.econav.core.common.decopmose.AppComponentContext

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
        val interactor = object : MapInterface {

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

            override val boundingBoxUpdateFlow: Flow<BoundingBoxUpdate> = boundingBoxFlow
        }

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

