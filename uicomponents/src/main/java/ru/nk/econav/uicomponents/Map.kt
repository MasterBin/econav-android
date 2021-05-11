package ru.nk.econav.uicomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

@Composable
fun Map() {
    val mapView = rememberMapView()
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {


            mapView
        }
    ) { m ->
        m.getMapAsync {
            it.getStyle {
//                it.addLayer(HeatmapLayer("layer-id", "source-id"))
//                HeatmapLayer("layer-id", "source-id").
            }
        }
    }
}

@Composable
fun rememberMapView() : MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val map = remember {
        Mapbox.getInstance(context, "sk.eyJ1IjoibmlraXRzYW1veWxvdiIsImEiOiJja29pejBkMDAxa3o4MnpzOXk2eWJldDBqIn0.FNVZkzdWqyJGpI0GeGys3A")

        MapView(context).apply {
            id = R.id.composeMap2
            onCreate(null)
        }
    }

    return map
}