package ru.nk.econav.mapscreen

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.google.android.gms.common.util.MapUtils
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.extended
import ru.nk.econav.extended_lifecycle.subscribe
import ru.nk.econav.mapscreen.MainMapComponent.Model.PathDrawn
import ru.nk.econav.mapscreen.MainMapComponent.Model.SelectPoints
import ru.nk.econav.mapscreen.data.LatLon


@Composable
fun Map(
    modifier: Modifier = Modifier,
    mainMap : MainMapComponent,
    lifecycleExtension: LifecycleExtension
) {
    val mapView = rememberMapView(lifecycleExtension = lifecycleExtension)
    val coroutineScope = rememberCoroutineScope()

    val modelG by mainMap.model.asState()
    var s by rememberSaveable { mutableStateOf(0) }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { view ->
            val model = modelG
            coroutineScope.launch {
                val map = view.awaitMap()
                when(model) {
                    is PathDrawn -> {
                        map.addPolyline {
                            addAll(PolyUtil.decode((model as PathDrawn).path))
                        }
                    }
                    is SelectPoints -> {
                        when {
                            (model as SelectPoints).from == null -> {
                                map.setOnMapLongClickListener {
                                    s = 1
                                    mainMap.events.setStartPoint(it.toLatLon())
                                }
                            }
                            (model as SelectPoints).to == null -> {
                                map.addMarker {
                                    position(LatLng((model as SelectPoints).from!!.lat, (model as SelectPoints).from!!.lon))
                                }
                                map.setOnMapLongClickListener {
                                    mainMap.events.setEndPoint(it.toLatLon())
                                }
                            }
                            else -> {
                                map.setOnMapLongClickListener(null)
                                map.addMarker {
                                    position(LatLng((model as SelectPoints).to!!.lat, (model as SelectPoints).to!!.lon))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun LatLng.toLatLon() =
    LatLon(
        lat = latitude,
        lon = longitude
    )

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
                ; Log.i("AAA","onCreate")
            },
            onStart = { mapView.onStart(); Log.i("AAA","onStart")},
            onResume = { mapView.onResume(); Log.i("AAA","onResume") },
            onPause = { mapView.onPause(); Log.i("AAA","onPause") },
            onStop = { mapView.onStop(); Log.i("AAA","onStop") },
            onDestroy = { mapView.onDestroy(); Log.i("AAA","onDestroy") },
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
