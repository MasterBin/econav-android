package ru.nk.econav.android.general.impl

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.google.accompanist.insets.imePadding
import kotlinx.coroutines.launch
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.ui.basic.CircleFloatingButton
import ru.nk.econav.ui.basic.RoundedButton
import ru.nk.econav.ui.basic.RoundedButtonWithIcon
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
            DrawerContentExpanded(component = component, swipeableState = swipeableState)
        },
        onDrawerContent = {
            OnDrawer(component = component)
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContentExpanded(
    component: GeneralComponentImpl,
    swipeableState : SwipeableState<DraggableBottomDrawerState>
) {
    val focusRequester = FocusRequester()

    Box(modifier = Modifier.fillMaxSize()) {
        OneChild(state = component.searchPlacesComponent.state) {
            LaunchedEffect(swipeableState) {
                if (swipeableState.currentValue == DraggableBottomDrawerState.General)
                    focusRequester.requestFocus()
            }

            it.instance.render(
                Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester = focusRequester)
            ).invoke()
        }

        RoundedButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 16.dp)
                .imePadding(),
            text = stringResource(id = R.string.choose_on_map),
            onClick = { component.chooseEndLocationOnMap() }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContent(
    component: GeneralComponentImpl,
    swipeableState: SwipeableState<DraggableBottomDrawerState>,
) {
    val scope = rememberCoroutineScope()

    val model by component.model.asState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
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
        Search(
            modifier = searchModifier,
            text = model.searchText,
            onTextChanged = {},
            enabled = false
        )

        Spacer(Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.secondary)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.starting_point),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.width(8.dp))

                if (model.startLocation != null) {
                    if (model.isUserLocation) {
                        RoundedButtonWithIcon(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = stringResource(R.string.currrent_geolocation),
                            icon = R.drawable.ic_point_start,
                            onClick = { component.onChooseStartPointClicked() }
                        )
                    } else {
                        RoundedButton(
                            Modifier.padding(horizontal = 8.dp),
                            text = stringResource(id = R.string.choosen_location_on_map),
                            onClick = { component.onChooseStartPointClicked() }
                        )
                        if (model.showUserLocationButton) {
                            CircleFloatingButton(onClick = { component.chooseUserLocation() }) {
                                Icon(
                                    painterResource(id = R.drawable.ic_point_start),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                        }
                    }
                } else {
                    RoundedButton(
                        Modifier.padding(horizontal = 8.dp),
                        text = stringResource(id = R.string.not_choosen),
                        onClick = { component.onChooseStartPointClicked() }
                    )
                    if (model.showUserLocationButton) {
                        CircleFloatingButton(onClick = { component.chooseUserLocation() }) {
                            Icon(
                                painterResource(id = R.drawable.ic_point_start),
                                contentDescription = null,
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OnDrawer(
    component: GeneralComponentImpl
) {
    val state by component.model.asState()

    if (state.showUserLocationButton) {
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
}