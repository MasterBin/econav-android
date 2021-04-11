package ru.nk.econav.android.eco_param_elector.impl

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.decopmose.Content

class EcoParamElectorImpl(
    private val componentContext: AppComponentContext,
    private val deps: EcoParamElector.Dependencies,
) : EcoParamElector, AppComponentContext by componentContext, EcoParamElector.Dependencies by deps {

    override fun renderer(modifier: Modifier): Content = {
        EcoParamElectorView(modifier = modifier, component = this)
    }

    private val _model = MutableValue(Model(deps.range.start))
    val floatRange = deps.range
    val model: Value<Model> = _model

    fun onValueChange(value: Float) {
        _model.reduce { it.copy(value = value) }
    }

    fun onValueChangeFinished() {
        valueChanged.invoke(model.value.value)
    }

    data class Model(
        val value: Float = 0f
    )
}

@Composable
fun EcoParamElectorView(
    modifier: Modifier,
    component: EcoParamElectorImpl
) {
    val model by component.model.asState()

    Slider(
        modifier = modifier,
        value = model.value,
        valueRange = component.floatRange,
        onValueChange = { component.onValueChange(it) },
        onValueChangeFinished = { component.onValueChangeFinished() }
    )
}