package ru.nk.econav.android.general.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.util.OutEvent

interface GeneralComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val userLocation : UserLocationComponent.Factory
        val searchPlaces : SearchPlacesComponent.Factory
    }

    interface Dependencies {
        val getMapInterface : GetMapInterface
        val placeSelected : OutEvent<GeoFeature>
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : GeneralComponent
    }
}