package ru.nk.econav.android.general.impl

import android.widget.Toast
import androidx.compose.ui.Modifier
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.android.general.impl.store.GeneralStore
import ru.nk.econav.android.general.impl.store.GeneralStore.Intent
import ru.nk.econav.android.general.impl.store.GeneralStoreProvider
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.core.common.util.asValue
import ru.nk.econav.core.common.util.getStore

class GeneralComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: GeneralComponent.Dependencies,
    private val children: GeneralComponent.Children
) : AppComponentContext by appComponentContext, GeneralComponent,
    GeneralComponent.Dependencies by deps {

    private val store = instanceKeeper.getStore {
        GeneralStoreProvider(DefaultStoreFactory)
            .create()
    }

    private val userLocationFlow = MutableSharedFlow<LatLon>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val userLocationComponent = oneChild("userLocation") { appContext ->
        children.userLocation.invoke(
            appContext,
            object : UserLocationComponent.Dependencies, GeneralComponent.Dependencies by deps {
                override val userLocation: OutEvent<LatLon> = OutEvent {
                    componentScope.launch {
                        userLocationFlow.emit(it)
                    }
                    if (!store.state.showUserLocationButton) {
                        store.accept(Intent.ShowUserLocationButton)
                    }
                    if (store.state.startLocation == null) {
                        store.accept(Intent.SetUserLocation(it))
                    }
                }
                override val permissionNotGranted: OutEvent<Unit> = OutEvent {
                    store.accept(Intent.PermissionNotGranted)
                }
            })
    }

    val searchPlacesComponent = oneChild("searchPlaces") { appContext ->
        children.searchPlaces.invoke(appContext, object : SearchPlacesComponent.Dependencies {
            override val userLocationFlow: Flow<LatLon> = this@GeneralComponentImpl.userLocationFlow
            override val placeSelected: OutEvent<GeoFeature> = OutEvent { featureSelected(it) }
            override val clearFlow: Flow<Unit> = MutableSharedFlow()
            override val textChanged: OutEvent<String> = OutEvent {
                store.accept(Intent.SearchTextChanged(it))
            }
        })
    }

    private val mapInterface = getMapInterface.invoke(lifecycle)

    private val overlayPoints = ItemizedIconOverlay<OverlayItem>(
        applicationContext,
        mutableListOf(),
        object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean { return false }
            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean { return false }
        }
    )

    val model = store.asValue()

    init {
        mapInterface.add(overlayPoints)

        selectedStartLocation.location?.let {
            store.accept(Intent.SelectedStartLocation(it))
        }

        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is GeneralStore.Label.RouteToPlace -> placeSelected.invoke(
                        GeneralComponent.Place(
                            it.startLocation,
                            it.place,
                            it.isUserLocation
                        )
                    )
                    GeneralStore.Label.NoStartLocation -> noStartLocation()
                }
            }
        }

        componentScope.launch {
            store.states
                .distinctUntilChanged { old, new ->
                    old.startLocation == new.startLocation &&
                    old.isUserLocation == new.isUserLocation
                }.collect {
                    overlayPoints.removeAllItems()
                    if (it.startLocation != null && !it.isUserLocation)
                        overlayPoints.addItem(
                            OverlayItem(
                                null, null,
                                GeoPoint(it.startLocation.lat,it.startLocation.lon)
                            ).apply {
                                setMarker(applicationContext.getDrawable(R.drawable.ic_point_start))
                            }
                        )

                    mapInterface.invalidateMap()
                }
        }
    }

    private fun noStartLocation() {
        Toast.makeText(
            applicationContext,
            "Пожалуйста, укажите начальную точку",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun featureSelected(geoFeature: GeoFeature) {
        store.accept(Intent.SelectedPlace(geoFeature))
    }

    fun setMapOffset(offset: Int) {
        mapInterface.setMapCenterOffset(
            0,
            (-offset / applicationContext.resources.displayMetrics.density).toInt()
        )
    }

    fun onChooseStartPointClicked() {
        chooseStartLocation.invoke(userLocationFlow.replayCache.firstOrNull())
    }

    fun chooseUserLocation() {
        val location = userLocationFlow.replayCache.firstOrNull()
        if (location != null)
            store.accept(Intent.SetUserLocation(location))
        else
            store.accept(Intent.PermissionNotGranted)
    }

    fun chooseEndLocationOnMap() {
        if (store.state.startLocation != null) {
            chooseEndLocation.invoke(
                GeneralComponent.StartLocation(
                    store.state.startLocation!!,
                    store.state.isUserLocation
                )
            )
        } else {
            noStartLocation()
        }
    }

    override fun render(modifier: Modifier): Content = { General(modifier, this) }
}



