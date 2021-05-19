package ru.nk.econav.android.features.place_details.impl

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import ru.nk.econav.core.common.decompose.OneChild
import ru.nk.econav.ui.components.DraggableBottomDrawer
import ru.nk.econav.ui.basic.RoundedButton

@Composable
fun PlaceDetails(
    modifier: Modifier = Modifier,
    component: PlaceDetailsComponentImpl
) {
    DraggableBottomDrawer(
        modifier = modifier.onSizeChanged {
            component.setMapOffset((it.height * 0.45).toInt())
        },
        freeze = true,
        drawerRatio = 0.3f,
        onDrawerContent = {
            OnDrawer(component = component)
        },
        drawerContent = {
            DrawerContent(component = component)
        }
    )
}

@Composable
fun DrawerContent(
    component: PlaceDetailsComponentImpl
) {
    val model by component.model.asState()

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 18.dp, end = 18.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = component.place.name,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = component.place.address,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Column(
                modifier = Modifier.wrapContentWidth(), horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier,
                    text = model.route?.distance ?: component.place.distanceTo ?: "",
                    style = MaterialTheme.typography.h5.copy(
                        if (model.route != null) {
                            MaterialTheme.colors.secondaryVariant
                        } else {
                            MaterialTheme.colors.primary
                        }
                    )
                )
                model.route?.let {
                    Text(
                        modifier = Modifier,
                        text = it.time,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
        RoundedButton(
            text = stringResource(R.string.route_to),
            onClick = { component.routeToClicked() }
        )
    }
}

@Composable
fun OnDrawer(
    component: PlaceDetailsComponentImpl
) {
    val model by component.model.asState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            if (model.showUserLocationButton) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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

            OneChild(state = component.ecoParamElector.state) {
                it.instance.renderer(Modifier.fillMaxWidth())
                    .invoke()
            }
        }
    }
}