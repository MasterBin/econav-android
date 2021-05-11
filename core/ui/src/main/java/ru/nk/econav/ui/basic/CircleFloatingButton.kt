package ru.nk.econav.ui.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CircleFloatingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentColor: Color = contentColorFor(MaterialTheme.colors.surface),
    content: @Composable () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Surface(
        modifier = modifier
            .size(48.dp, 48.dp)
            .clickable(
                interactionSource = interactionSource,
                role = Role.Button,
                indication = null,
                onClick = onClick
            ),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation()
            .elevation(interactionSource = interactionSource).value,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.indication(interactionSource, rememberRipple()),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}