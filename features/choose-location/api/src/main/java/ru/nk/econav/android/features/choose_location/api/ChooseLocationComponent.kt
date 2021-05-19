package ru.nk.econav.android.features.choose_location.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

interface ChooseLocationComponent {

    fun render(modifier : Modifier) : Content

    interface Dependencies {
        val startWith : LatLon?
        val getMapInterface : GetMapInterface
        val locationConfirmed : OutEvent<LatLon>
        val isStartLocationChoosing : Boolean
    }

    interface Children {
        val searchPlaces : SearchPlacesComponent.Factory
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : ChooseLocationComponent
    }

}