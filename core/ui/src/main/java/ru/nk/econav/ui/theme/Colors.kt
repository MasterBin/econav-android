package ru.nk.econav.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

internal val DarkColors = darkColors(
    primary = Color(0, 0, 0),
    surface = Color.White
)

internal val LightColors = lightColors(
    primary = Color.Black,
    primaryVariant = Color(0xff44CCBB), //TODO
    secondary = Color(0xffD5DDDD),
    secondaryVariant = Color(0xffFF873E), // TODO
    background = Color(0xff00564E),
    surface = Color.White,
    error = Color.Red, //TODO
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)