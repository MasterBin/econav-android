package ru.nk.econav.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    appContent : @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        shapes = AppShapes
    ) {
        appContent()
    }
}