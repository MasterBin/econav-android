package ru.nk.econav.android.features.choose_location.impl

import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.choose_location.api.ChooseLocationComponent
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

class ChooseLocationComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: ChooseLocationComponent.Dependencies,
    private val children: ChooseLocationComponent.Children
) : ChooseLocationComponent, ChooseLocationComponent.Dependencies by deps,
    AppComponentContext by appComponentContext {

    private val clearSearch = MutableSharedFlow<Unit>()
    private val mapInterface = getMapInterface(lifecycle)
    private val locationSearch = MutableSharedFlow<LatLon>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val searchPlacesComponent = oneChild("searchPlaces") {
        children.searchPlaces.invoke(
            it,
            object : SearchPlacesComponent.Dependencies,
                ChooseLocationComponent.Dependencies by this {
                override val userLocationFlow: Flow<LatLon> = locationSearch
                override val placeSelected: OutEvent<GeoFeature> = OutEvent { f ->
                    moveCameraTo(f.center)
                    clearSearch()
                }
                override val clearFlow: Flow<Unit> = clearSearch
            })
    }

    init {
        startWith?.let { moveCameraTo(startWith) }
        componentScope.launch {
            mapInterface.boundingBoxUpdateFlow.collect { bb ->
                bb.boundingBox.let {
                    locationSearch.emit(LatLon(it.centerLatitude, it.centerLongitude))
                }
            }
        }
    }

    private val _looseFocus = MutableSharedFlow<Unit>()
    val looseFocus : Flow<Unit> = _looseFocus

    override fun render(modifier: Modifier): Content = {
        ChooseLocation(modifier = modifier, component = this)
    }


    private fun clearSearch() {
        componentScope.launch {
            clearSearch.emit(Unit)
        }
    }

    fun selectLocation() {
        locationSearch.replayCache.firstOrNull()?.let {
            deps.locationConfirmed.invoke(it)

            componentScope.launch {
                _looseFocus.emit(Unit)
            }
        }
    }

    private fun moveCameraTo(point: LatLon) {
        mapInterface.moveToPoint(GeoPoint(point.lat, point.lon), zoom = 17.0)
    }
}