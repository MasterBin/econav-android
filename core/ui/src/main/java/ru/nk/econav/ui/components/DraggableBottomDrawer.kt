package ru.nk.econav.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    drawerContent: @Composable () -> Unit = @Composable {},
    drawerContentExpanded: @Composable () -> Unit = @Composable {},
    onDrawerContent: @Composable () -> Unit = @Composable {}
) {
    val swipeableState = rememberSwipeableState(1)

    BoxWithConstraints(modifier.fillMaxSize()) {
        val maxHeight2 = with(LocalDensity.current) {
            constraints.maxHeight.toDp()
        }
        val fullHeight = constraints.maxHeight.toFloat()
        val maxHeight by remember { mutableStateOf(maxHeight2) }
        val openPercent = 0.5f

        val anchors = mapOf(
            0f to 0,
            fullHeight * openPercent to 1,
            fullHeight to 2
        )
        val swipeableModifier = Modifier.swipeable(
            state = swipeableState,
            anchors = anchors,
            enabled = true,
            thresholds = { from, to ->
                if (to == 2)
                    FixedThreshold(1000000.dp)
                else
                    FractionalThreshold(0.2f)
            },
            orientation = Orientation.Vertical
        )

        Column(
            Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(x = 0, y = swipeableState.offset.value.roundToInt())
                },
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                visible = (swipeableState.offset.value >= fullHeight * openPercent),
                initiallyVisible = true
            ) {
                onDrawerContent()
            }
            Surface(
                swipeableModifier.fillMaxSize(),
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
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(if (swipeableState.offset.value >= fullHeight * openPercent) 1 - openPercent else 1f)
                    ) {
                        Crossfade(targetState = swipeableState.offset.value >= fullHeight * openPercent) {
                            if(it) {
                                Box(Modifier.fillMaxSize().background(Color.Red))
                            } else {
                                Box(Modifier.fillMaxSize().background(Color.Green))
                            }
                        }
                    }
                }
            }
        }
    }

}