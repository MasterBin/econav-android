package ru.nk.econav.android.eco_param_elector.api

import androidx.compose.ui.Modifier
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.util.OutEvent

interface EcoParamElector {

    fun renderer(modifier : Modifier) : Content

    interface Dependencies {
        val range : ClosedFloatingPointRange<Float>
        val valueChanged : OutEvent<Float>
    }

    fun interface Factory {
        fun invoke(componentContext : AppComponentContext, deps : Dependencies) : EcoParamElector
    }
}
