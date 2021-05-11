package ru.nk.econav.android.eco_param_elector.impl

import android.os.Parcelable
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.statekeeper.consume
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import kotlinx.parcelize.Parcelize
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content

class EcoParamElectorImpl(
    private val componentContext: AppComponentContext,
    private val deps: EcoParamElector.Dependencies,
) : EcoParamElector, AppComponentContext by componentContext, EcoParamElector.Dependencies by deps {

    override fun renderer(modifier: Modifier): Content = {
        EcoParamElectorView(modifier = modifier, component = this)
    }

    private val state = stateKeeper.consume<Model>("SAVED") ?: Model(deps.range.start)

    init {
        stateKeeper.register("SAVED") {
            model.value
        }
    }

    private val _model = MutableValue(state)
    val floatRange = deps.range
    val model: Value<Model> = _model

    fun onValueChange(value: Float) {
        _model.reduce { it.copy(value = value) }
    }

    fun onValueChangeFinished() {
        valueChanged.invoke(model.value.value)
    }

    @Parcelize
    data class Model(
        val value: Float = 0f
    ) : Parcelable
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