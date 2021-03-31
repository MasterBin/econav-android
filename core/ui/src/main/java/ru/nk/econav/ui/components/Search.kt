package ru.nk.econav.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.nk.econav.ui.R
import ru.nk.econav.ui.basic.TextFieldWithHint
import ru.nk.econav.ui.theme.AppTheme

@Composable
fun Search(
    modifier : Modifier = Modifier,
    hintText: String,
    text: String,
    onTextChanged: (String) -> Unit
) {
    Surface(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = 20.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
            )
            TextFieldWithHint(
                value = text,
                hintText = hintText,
                onValueChange = onTextChanged,
                maxLines = 1
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    AppTheme {
        var text by remember {
            mutableStateOf("")
        }
        Search(
            hintText = "Search",
            text = text,
            onTextChanged = { text = it }
        )
    }
}