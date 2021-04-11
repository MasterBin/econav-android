package ru.nk.econav.mapscreen

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.extended
import ru.nk.econav.extended_lifecycle.subscribe
import ru.nk.econav.mapscreen.map.MapComponent


@Composable
fun Map(
    modifier: Modifier = Modifier,
    mainMap: MapComponent,
    lifecycleExtension: LifecycleExtension
) {
    val mapView = rememberMapView(lifecycleExtension = lifecycleExtension)
    val coroutineScope = rememberCoroutineScope()

    val modelG by remember { mutableStateOf(false) }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView
        },
        update = { view ->
            val model = modelG
            coroutineScope.launch {
                val map = view.awaitMap()
//                mainMap.onMapCreated(map)
            }
        }
    )
}

//private fun GoogleMap.logic(component: MapComponent, model: MapComponent.Model) {
//    //TODO: разобраться как не пересоздавать манагеров
//    val markerManager = MarkerManager(this)
//    val groundOverlayManager = GroundOverlayManager(this)
//    val polygonManager = PolygonManager(this)
//    val polylineManager = PolylineManager(this)
//
//    this.cameraPosition
//    this.projection
//
//    if (model.createLayer) {
//        component.createLayer(
//            markerManager.newCollection(),
//            polygonManager.newCollection(),
//            polylineManager.newCollection(),
//            groundOverlayManager.newCollection()
//        )
//        return
//    }
//}

@Composable
private fun rememberMapView(
    lifecycleExtension: LifecycleExtension
): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            id = R.id.composeMap
        }
    }

    val bundle by rememberSaveable { mutableStateOf(Bundle()) }

    val extLifecycle = remember { lifecycle.asDecomposeLifecycle().extended(lifecycleExtension) }

    DisposableEffect(lifecycle, effect = {
        extLifecycle.subscribe(
            onCreate = { _ ->
                mapView.onCreate(bundle)
            },
            onStart = { mapView.onStart() },
            onResume = { mapView.onResume() },
            onPause = { mapView.onPause() },
            onStop = { mapView.onStop() },
            onDestroy = { mapView.onDestroy() },
            onLowMemory = { mapView.onLowMemory() },
            onSaveInstanceState = { _ ->
                mapView.onSaveInstanceState(bundle)
            }
        )
        onDispose {
            extLifecycle.dispose()
        }
    })

    return mapView
}
