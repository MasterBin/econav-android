package ru.nk.econav.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nk.econav.android.core.resources.R
import ru.nk.econav.ui.basic.TextFieldWithHint

@Composable
fun Search(
    modifier: Modifier = Modifier,
    hintText: String = stringResource(R.string.search),
    text: String,
    enabled: Boolean = true,
    onTextChanged: (String) -> Unit
) {
    Surface(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.secondary),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = contentColorFor(MaterialTheme.colors.surface)
            )
            if (enabled) {
                TextFieldWithHint(
                    value = text,
                    hintText = hintText,
                    onValueChange = onTextChanged,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.subtitle1
                )
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = hintText,
                    maxLines = 1,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    var text by remember {
        mutableStateOf("")
    }
    Search(
        hintText = "Search",
        text = text,
        onTextChanged = { text = it }
    )
}