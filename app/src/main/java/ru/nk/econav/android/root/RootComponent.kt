package ru.nk.econav.android.root

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import ru.nk.econav.android.R
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.core.common.decompose.oneChild


class RootComponent(
    private val componentContext: AppComponentContext
) : AppComponentContext by componentContext, KoinComponent {

    private val mapComponent = oneChild("map") {
        getKoin().get<MapComponent.Factory>().invoke(it)
    }

    val mapComponentState = mapComponent.state
}

@Composable
fun Root(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    Box(modifier) {
        OneChild(state = component.mapComponentState) {
            it.instance.render(
                modifier = Modifier
                    .fillMaxWidth()
            ).invoke()
        }
    }
}