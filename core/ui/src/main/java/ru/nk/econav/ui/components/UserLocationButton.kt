package ru.nk.econav.ui.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.nk.econav.ui.R
import ru.nk.econav.ui.basic.CircleFloatingButton

@Composable
fun UserLocationButton(
    modifier : Modifier = Modifier,
    onClick : () -> Unit
) {
    CircleFloatingButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location_arrow),
            contentDescription = "compass"
        )
    }
}