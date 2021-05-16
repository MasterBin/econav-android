package ru.nk.econav.uicomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.nk.econav.ui.components.*
import ru.nk.econav.ui.theme.AppTheme
import kotlin.math.roundToInt

private val initScreen = Screen.Little

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Column {
                    var screen by remember {
                        mutableStateOf(initScreen)
                    }
                    ScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        selectedTabIndex = screen.index,
                    ) {
                        Screen.values().forEachIndexed { i, it ->
                            Tab(
                                selected = i == screen.index,
                                onClick = { screen = it },
                                text = { Text(it.name) }
                            )
                        }
                    }
                    Surface(Modifier.fillMaxSize()) {
                        when (screen) {
                            Screen.Little -> LittleScreen()
                            Screen.BottomDrawer -> BottomDrawer()
                            Screen.Map -> Map()
                        }
                    }
                }
            }
        }
    }
}

enum class Screen(val index: Int) {
    Little(0), BottomDrawer(1), Map(2)
}

@Composable
fun LittleScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var text by remember {
            mutableStateOf("")
        }
        Search(
            hintText = "Search",
            text = text,
            onTextChanged = {
                text = it
            }
        )
        SwipeableSample()
        CompassButton() {}
        UserLocationButton() {}
        SearchItem(
            title = "The Mayor asdlkjaslkdfa alksdjf; asdfl a;sldk a;sdl asl;d f",
            subtitle = "8502 Preston Rd. Inglewood, Maine 98380",
            distanceTo = "12212 m"
        ) {}
        RoundedButton(text = "Route to") {}

        var value by remember {
            mutableStateOf(0f)
        }
        EcoParamSlider(value = value, onValueChange = { value = it })
    }
}

@Composable
fun BottomDrawer() {
    var hidden by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            generateSequence("") { it + "1" }.take(100).forEach {
                Text(it)
            }
        }
        DraggableBottomDrawer(
            modifier = Modifier.fillMaxSize(),
            hidden = hidden,
            onDrawerContent = {
                Column() {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            hidden = !hidden
                        },
                    ) {
                        Text(text = "HIDE/SHOW")
                    }
                    Spacer(Modifier.height(24.dp))
                }
            },
            drawerContent = {
                BoxWithConstraints(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) {
                    Text("manHeight is $maxHeight")
                    Text(modifier = Modifier.align(Alignment.BottomCenter), text = "HELLO")
                }
            },
            drawerContentExpanded = {
                BoxWithConstraints(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text("manHeight is $maxHeight")
                    Text(modifier = Modifier.align(Alignment.BottomCenter), text = "HELLO")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { from, to -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}

@Preview
@Composable
fun Preview() {
    AppTheme {
        SwipeableSample()
    }
}