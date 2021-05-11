package ru.nk.econav.android.userlocation

import android.location.Location
import androidx.compose.ui.Modifier
import ru.nk.econav.android.core.mapinterface.GetMapInterface
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.util.OutEvent

interface UserLocationComponent {

    fun render(modifier : Modifier) : Content

    interface Dependencies {
        val getMapInterface : GetMapInterface
        val userLocation : OutEvent<Location>
    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : UserLocationComponent
    }
}