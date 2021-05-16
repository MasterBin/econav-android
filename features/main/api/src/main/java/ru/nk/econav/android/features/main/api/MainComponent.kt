package ru.nk.econav.android.features.main.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content

interface MainComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val general : GeneralComponent.Factory
        val placeDetails : PlaceDetailsComponent.Factory
    }

    interface Dependencies {
        val getMapInterface : GetMapInterface
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, dependencies: Dependencies) : MainComponent
    }
}