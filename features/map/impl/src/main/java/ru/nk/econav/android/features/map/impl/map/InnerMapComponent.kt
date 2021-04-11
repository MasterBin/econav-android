package ru.nk.econav.android.features.map.impl.map

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.DefaultLifecycleCallbacks
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Overlay
import ru.nk.econav.android.core.mapinterface.MapInterface

class InnerMapComponent(
    private val componentContext : ComponentContext
) : ComponentContext by componentContext {

    private val coroutineScope = MainScope()

    init {
        lifecycle.subscribe(
            onDestroy = {
                coroutineScope.cancel()
            }
        )
    }

    private lateinit var context : Context

    val overlaysFlow = MutableStateFlow<List<Overlay>>(listOf())
    val invalidateMapFlow = MutableSharedFlow<Unit>()

    fun initContext(context : Context) {
        this.context = context
    }

    fun createInterface(lifecycle: Lifecycle) : MapInterface {
        val interactor = object : MapInterface {

            var mutableOverlays = mutableListOf<Overlay>()

            override fun add(overlay: Overlay) {
                coroutineScope.launch {
                    mutableOverlays.add(overlay)
                    overlaysFlow.emit(overlaysFlow.value.plus(overlay))
                }
            }

            override fun remove(overlay: Overlay) {
                coroutineScope.launch {
                    mutableOverlays.remove(overlay)
                    overlaysFlow.emit(overlaysFlow.value.minus(overlay))
                }
            }

            override fun invalidateMap() {
                coroutineScope.launch {
                    invalidateMapFlow.emit(Unit)
                }
            }

        }

        val listener = object : DefaultLifecycleCallbacks {
            override fun onDestroy() {
                coroutineScope.launch {
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

