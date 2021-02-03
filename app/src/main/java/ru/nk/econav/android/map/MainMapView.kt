package ru.nk.econav.android.map

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.extensions.android.layoutInflater
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.addPolyline
import com.google.maps.android.ktx.model.cameraPosition
import ru.nk.econav.android.databinding.ViewMainMapBinding
import ru.nk.econav.extended_lifecycle.ExtendedLifecycle
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.extended
import ru.nk.econav.extended_lifecycle.subscribe

@Suppress("functionName")
fun ViewContext.MainMapView(
    model: MainMap.Model,
    lifecycleExtension: LifecycleExtension
): View {

    val vb = ViewMainMapBinding.inflate(layoutInflater)

    lifecycle.extended(lifecycleExtension).handleLifecycle(vb.mapView)

    vb.mapView.getMapAsync { map ->
        model.data.subscribe {
            when (it.state) {
                MainMap.State.None -> {
                    map.clear()
                }
                MainMap.State.Some -> {
                    if (it.startPoint != null) {
                        map.addMarker {
                            position(it.startPoint)
                            title("Start")
                        }
                    }
                    if (it.endPoint != null) {
                        map.addMarker {
                            position(it.endPoint)
                            title("End")
                        }
                    }
                }
                MainMap.State.LoadingPath -> {

                }
                is MainMap.State.PathLoaded -> {
                    val f = it.state.formattedPath.map {
                        PolyUtil.decode(it)
                    }.apply {
                        forEachIndexed { index, it ->
                            map.addPolyline {
                                addAll(it)
                                color(when(index % 6) {
                                    0 -> Color.BLACK
                                    1 -> Color.BLUE
                                    2 -> Color.GREEN
                                    3 -> Color.RED
                                    4 -> Color.YELLOW
                                    else -> Color.MAGENTA
                                })
                            }
                        }
                    }

                    map.animateCamera(
                        CameraUpdateFactory
                            .newCameraPosition(cameraPosition {
                                target(f[0][0])
                                zoom(12f)
                            })
                    )
                }
            }
        }

        map.setOnMapLongClickListener { ll ->
            model.data.value.let {
                if (it.state != MainMap.State.LoadingPath) {
                    when {
                        it.startPoint == null -> {
                            model.setStartPoint(ll)
                        }
                        it.endPoint == null -> {
                            model.setEndPoint(ll)
                        }
                        else -> {
                            model.clearAll()
                        }
                    }
                }
            }

        }
    }

    return vb.root
}


private fun ExtendedLifecycle.handleLifecycle(mapView: MapView) {
    subscribe(
        onCreate = { savedInstanceState ->
            val mapViewBundle = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
            mapView.onCreate(mapViewBundle)
        },
        onStart = { mapView.onStart() },
        onResume = { mapView.onResume() },
        onPause = { mapView.onPause() },
        onStop = { mapView.onStop() },
        onDestroy = { mapView.onDestroy() },
        onLowMemory = { mapView.onLowMemory() },
        onSaveInstanceState = { outState ->
            val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().apply {
                outState.putBundle(MAPVIEW_BUNDLE_KEY, this)
            }
            mapView.onSaveInstanceState(mapViewBundle)
        }
    )
}

private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
