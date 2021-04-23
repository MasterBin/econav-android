package ru.nk.econav.android.features.map.impl.map

import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
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
            getInstance().load(
                appContext,
                PreferenceManager.getDefaultSharedPreferences(appContext)
            )
            getInstance().userAgentValue = "ru.nk.econav.android"

            mapView.translateBoundingBox(component)

            //FIXME: overlays overlap problem
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

private fun MapView.translateBoundingBox(component: InnerMapComponent) {
    addOnFirstLayoutListener { _, _, _, _, _ ->
        post { component.updateBoundingBox(boundingBox, false) }
    }

    addMapListener(DelayedMapListener(object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            post { component.updateBoundingBox(boundingBox, false) }
            return false
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            post { component.updateBoundingBox(boundingBox, true) }
            return false
        }

    }, 200))
}

@Composable
private fun rememberMapView(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            id = R.id.composeMap
            setDestroyMode(false)
            setTileSource(TileSourceFactory.MAPNIK)
//            isTilesScaledToDpi = true
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            setMultiTouchControls(true)
        }
    }

    var mapData by rememberSaveable { mutableStateOf(MapUserData()) }

    DisposableEffect(lifecycle, effect = {
        lifecycle.asDecomposeLifecycle().subscribe(
            onCreate = {
                mapView.controller.setZoom(mapData.zoom)
                mapView.setExpectedCenter(
                    GeoPoint(
                        mapData.latitudeString.toDouble(),
                        mapData.longitudeString.toDouble()
                    )
                )
            },
            onResume = {
                mapView.onResume()
            },
            onPause = {
                mapData = mapData.copy(
                    zoom = mapView.zoomLevelDouble,
                    latitudeString = mapView.mapCenter.latitude.toString(),
                    longitudeString = mapView.mapCenter.longitude.toString()
                )
                mapView.onPause()
            }
        )

        onDispose {
            mapView.onDetach()
        }
    })

    return mapView
}

@Parcelize
private data class MapUserData(
    val zoom: Double = 8.0,
    val orientation: Float = 0f,
    val latitudeString: String = "55.752121",
    val longitudeString: String = "37.617664",
) : Parcelable