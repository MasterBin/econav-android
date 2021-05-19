package ru.nk.econav.android.features.choose_location.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.ui.basic.RoundedButtonWithIcon


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChooseLocation(
    modifier: Modifier,
    component: ChooseLocationComponentImpl
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(1.5f)
                .padding(bottom = 54.dp),
            painter = painterResource(
                id = if (component.isStartLocationChoosing)
                    R.drawable.ic_point_start
                else
                    R.drawable.ic_point_end
            ),
            contentDescription = null,
            tint = Color.Unspecified
        )

        RoundedButtonWithIcon(
            modifier = Modifier
                .padding(horizontal = 56.dp, vertical = 32.dp)
                .align(Alignment.BottomCenter),
            onClick = { component.selectLocation() },
            text = stringResource(id = R.string.confirm),
            icon = R.drawable.ic_confirm
        )

        Surface(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .sizeIn(maxHeight = 360.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            OneChild(state = component.searchPlacesComponent.state) {
                it.instance.render(
                    Modifier
                        .padding(vertical = 16.dp)
                ).invoke()
            }
        }
    }
}
