package ru.nk.econav.ui.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import ru.nk.econav.ui.R
import ru.nk.econav.ui.basic.CircleFloatingButton

@Composable
fun CompassButton(
    modifier : Modifier = Modifier,
    onClick : () -> Unit
) {
    CircleFloatingButton(modifier = modifier,
        onClick = onClick,
        contentColor = Color.Transparent
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_compass),
            tint = Color.Unspecified,
            contentDescription = "compass"
        )
    }
}