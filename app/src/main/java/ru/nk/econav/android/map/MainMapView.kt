package ru.nk.econav.android.map

import android.os.Bundle
import android.view.View
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.extensions.android.layoutInflater
import com.arkivanov.decompose.value.ObserveLifecycleMode
import com.arkivanov.decompose.value.observe
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import org.koin.java.KoinJavaComponent.getKoin
import ru.nk.econav.android.databinding.ViewMainMapBinding
import ru.nk.econav.extended_lifecycle.ExtendedLifecycle
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.extended
import ru.nk.econav.extended_lifecycle.subscribe

@Suppress("functionName")
fun ViewContext.MainMapView(
    model: MainMap.Model
): View {
    val lifecycleExtension: LifecycleExtension = getKoin().get()
    val vb = ViewMainMapBinding.inflate(layoutInflater)

    lifecycle.extended(lifecycleExtension).handleLifecycle(vb.mapView)

//    model.data.observe(lifecycle) {
//        vb.text.text = it.text
//    }

    vb.mapView.getMapAsync {
        it.addMarker(MarkerOptions().apply {
            position(LatLng(55.754079, 37.618879))
        }).apply {
            model.data.observe(lifecycle) {
                title = it.text
                this.showInfoWindow()
            }

        }

        it.addPolyline(PolylineOptions().apply {

        })
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
