package ru.nk.econav.android.features.map.impl.map

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import ru.nk.econav.android.features.map.impl.R


@Composable
fun OsmMap(
    modifier: Modifier = Modifier,
    component: InnerMapComponent
) {
    val mapView = rememberMapView()
    val scope = rememberCoroutineScope()

    val appContext = LocalContext.current.applicationContext

    AndroidView(
        modifier = modifier,
        factory = { context ->
            component.initContext(context)

            getInstance().load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext))
            getInstance().userAgentValue = "ru.nk.econav.android"

            //FIXME:
            mapView.apply {
                scope.launch {
                    component.overlaysFlow.collect {
                        overlays.removeAll(overlays.subtract(it))
                        overlays.addAll(it.subtract(overlays))
                    }
                }
                scope.launch {
                    component.invalidateMapFlow.collect {
                        postInvalidateDelayed(20)
                    }
                }
            }
        },
    )
}

@Composable
private fun rememberMapView(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            id = R.id.composeMap
            setTileSource(TileSourceFactory.MAPNIK)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            setMultiTouchControls(true)

            controller.setCenter(GeoPoint(55.752121, 37.617664))
            controller.setZoom(8.0)
        }
    }

    DisposableEffect(lifecycle, effect = {
        lifecycle.asDecomposeLifecycle().subscribe(
            onResume = {
                mapView.onResume()
            },
            onPause = {
                mapView.onPause()
            }
        )

        onDispose {
            mapView.onDetach()
        }
    })

    return mapView
}
