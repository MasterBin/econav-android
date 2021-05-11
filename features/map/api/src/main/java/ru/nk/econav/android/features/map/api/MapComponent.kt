package ru.nk.econav.android.features.map.api

import androidx.compose.ui.Modifier
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content

interface MapComponent {

    fun render(modifier : Modifier) : Content

    interface Children {
        val mainComponent : MainComponent.Factory
    }

    fun interface Factory {
        fun invoke(componentContext : AppComponentContext) : MapComponent
    }
}