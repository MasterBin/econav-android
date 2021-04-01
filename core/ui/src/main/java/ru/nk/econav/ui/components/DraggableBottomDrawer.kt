package ru.nk.econav.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SwipeableDefaults.resistanceConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.nk.econav.ui.R
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun DraggableBottomDrawer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 20.dp,
    drawerRatio: Float = 0.5f,
    drawerContent: @Composable () -> Unit = @Composable {},
    drawerContentExpanded: @Composable () -> Unit = @Composable {},
    onDrawerContent: @Composable () -> Unit = @Composable {}
) {
    val swipeableState = rememberSwipeableState(1)

    BoxWithConstraints(modifier.fillMaxSize()) {
        val fullHeight = constraints.maxHeight.toFloat()

        val anchors = mapOf(
            0f to 0,
            fullHeight * drawerRatio to 1,
        )

        val swipeableModifier = Modifier.swipeable(
            state = swipeableState,
            anchors = anchors,
            enabled = true,
            thresholds = { _, _ -> FractionalThreshold(0.3f) },
            resistance = resistanceConfig(anchors.keys, 0f, 10f),
            orientation = Orientation.Vertical
        )

        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)

                        layout(placeable.width, placeable.height) {
                            placeable.placeRelativeWithLayer(
                                0,
                                (swipeableState.offset.value - placeable.height).roundToInt()
                            )
                        }
                    }
                    .align(Alignment.TopCenter),
                visible = (swipeableState.offset.value >= fullHeight * drawerRatio),
                initiallyVisible = true
            ) {
                onDrawerContent()
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(x = 0, y = swipeableState.offset.value.roundToInt())
                    },
                verticalArrangement = Arrangement.Top
            ) {
                Surface(
                    swipeableModifier
                        .fillMaxWidth()
                        .fillMaxHeight(if (swipeableState.offset.value >= fullHeight * drawerRatio) 1 - drawerRatio else 1f),
                    shape = shape,
                    color = color,
                    elevation = elevation,
                    contentColor = contentColor,
                    border = border
                ) {
                    Column(
                        Modifier.fillMaxSize()
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bottom_drawer_arrow),
                                contentDescription = null
                            )
                        }
                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            Crossfade(targetState = swipeableState.offset.value >= fullHeight * drawerRatio) {
                                if (it) {
                                    drawerContent()
                                } else {
                                    drawerContentExpanded()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}