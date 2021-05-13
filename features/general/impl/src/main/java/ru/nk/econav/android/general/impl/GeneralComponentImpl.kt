package ru.nk.econav.android.general.impl

import android.location.Location
import android.widget.Toast
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.lifecycle.subscribe
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.ui.components.DraggableBottomDrawerState

class GeneralComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: GeneralComponent.Dependencies,
    private val children : GeneralComponent.Children
) : AppComponentContext by appComponentContext, GeneralComponent,
    GeneralComponent.Dependencies by deps {

    val userLocationComponent = oneChild("userLocation") {
        children.userLocation.invoke(it, object : UserLocationComponent.Dependencies, GeneralComponent.Dependencies by deps {
            override val userLocation: OutEvent<Location> = OutEvent {
                componentScope.launch {
                    userLocationFlow.emit(LatLon(it.latitude, it.longitude))
                }
            }
        })
    }

    private val userLocationFlow = MutableSharedFlow<LatLon>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val searchPlacesComponent = oneChild("searchPlaces") {
        children.searchPlaces.invoke(it, object : SearchPlacesComponent.Dependencies {
            override val userLocationFlow: Flow<LatLon> = this@GeneralComponentImpl.userLocationFlow
            override val foundItem: OutEvent<GeoFeature> = OutEvent { featureSelected(it) }
        })
    }

    private val mapInterface = getMapInterface.invoke(lifecycle)

    init {
        lifecycle.subscribe(onDestroy = {
            setMapOffset(0)
        })
    }

    fun featureSelected(geoFeature: GeoFeature) {

    }

    fun setMapOffset(offset : Int) {
        mapInterface.setMapCenterOffset(0, (-offset / applicationContext.resources.displayMetrics.density).toInt() )
    }

    override fun render(modifier: Modifier): Content = { General(modifier, this) }
}

