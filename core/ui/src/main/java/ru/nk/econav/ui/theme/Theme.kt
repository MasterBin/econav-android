package ru.nk.econav.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    appContent : @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colors = colors,
        shapes = AppShapes,
        typography = typography
    ) {
        appContent()
    }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
//        systemUiController.setStatusBarColor(
//
//        )

        systemUiController.setNavigationBarColor(
            color = colors.surface,
            darkIcons = !darkTheme
        )
    }
}