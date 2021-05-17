package ru.nk.econav.android.general.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.launch
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.ui.components.DraggableBottomDrawer
import ru.nk.econav.ui.components.DraggableBottomDrawerState
import ru.nk.econav.ui.components.Search

const val drawerRatio = 0.3f

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun General(
    modifier: Modifier,
    component: GeneralComponentImpl
) {
    val swipeableState = rememberSwipeableState(DraggableBottomDrawerState.General)

    DraggableBottomDrawer(
        modifier = modifier.onSizeChanged {
            component.setMapOffset((it.height * 0.3).toInt())
        },
        swipeableState = swipeableState,
        drawerRatio = drawerRatio,
        drawerContent = {
            DrawerContent(component = component, swipeableState = swipeableState)
        },
        drawerContentExpanded = {
            DrawerContentExpanded(component = component)
        },
        onDrawerContent = {
            OnDrawer(component = component)
        }
    )
}


@Composable
fun DrawerContentExpanded(
    component: GeneralComponentImpl
) {
    val focusRequester = FocusRequester()

    OneChild(state = component.searchPlacesComponent.state) {
        SideEffect {
            focusRequester.requestFocus()
        }

        it.instance.render(
            Modifier.fillMaxSize().focusRequester(focusRequester = focusRequester)
        ).invoke()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContent(
    component: GeneralComponentImpl,
    swipeableState: SwipeableState<DraggableBottomDrawerState>
) {
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        val searchModifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    scope.launch {
                        swipeableState.animateTo(DraggableBottomDrawerState.Expanded)
                    }
                }
            )
            .padding(horizontal = 16.dp)

        Search(modifier = searchModifier, text = "", onTextChanged = {}, enabled = false)

    }
}


@Composable
fun OnDrawer(
    component: GeneralComponentImpl
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        OneChild(state = component.userLocationComponent.state) {
            it.instance
                .render(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp)
                )
                .invoke()
        }
    }
}