package ru.nk.econav.android.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.statekeeper.consume
import com.arkivanov.decompose.value.MutableValue
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.core.common.decompose.oneChild


class RootComponent(
    private val componentContext: AppComponentContext
) : AppComponentContext by componentContext, KoinComponent {

    private val mainComponent = oneChild("main") {
        getKoin().get<MapComponent.Factory>().invoke(it)
    }

    val text = stateKeeper.consume("text") ?: DA("Text")
    val model = MutableValue(text)

    init {
        stateKeeper.register("text") {
            model.value
        }
    }

    @kotlinx.parcelize.Parcelize
    data class DA(val text : String) : android.os.Parcelable

    val mainComponentState = mainComponent.state


    fun foo() {
        if (mainComponent.showed) {
            mainComponent.hide()

        } else {
            mainComponent.show()
        }
    }
}

@Composable
fun Root(
    modifier: Modifier = Modifier,
    component: RootComponent
) {

    Box(modifier) {
        OneChild(state = component.mainComponentState) {
            it.instance.render(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            )()
        }

        Button(onClick = { component.foo() }) {
            Text("Text")
        }
    }
}