package ru.nk.econav.android.features.searchplaces.api

import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent

interface SearchPlacesComponent {

    fun render(modifier : Modifier) : Content

    interface Dependencies {
        val userLocationFlow : Flow<LatLon>
        val placeSelected : OutEvent<GeoFeature>
        val textChanged : OutEvent<String>
            get() = OutEvent {  }
        val clearFlow : Flow<Unit>
            get() = flow {}
    }

    fun interface Factory {
        fun invoke(appComponentContext : AppComponentContext, deps : Dependencies) : SearchPlacesComponent
    }

}