package ru.nk.econav.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButton(
   modifier : Modifier = Modifier,
   text : String,
   onClick : () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primaryVariant
        ),
        contentPadding = PaddingValues(horizontal = 52.dp,vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button
        )
    }
}