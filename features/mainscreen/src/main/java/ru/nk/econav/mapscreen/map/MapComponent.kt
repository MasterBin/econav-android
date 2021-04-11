package ru.nk.econav.mapscreen.map

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.Overlay
import ru.nk.econav.mapscreen.MapInterface

class MapComponent(
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

    val overlaysFlow = MutableStateFlow<List<Overlay>>(listOf())

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
        }

        lifecycle.subscribe(
            onDestroy = {
                coroutineScope.launch {
                    overlaysFlow.emit(overlaysFlow.value.minus(interactor.mutableOverlays))
                }
            }
        )

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

