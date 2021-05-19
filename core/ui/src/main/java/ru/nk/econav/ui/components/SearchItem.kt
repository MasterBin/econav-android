package ru.nk.econav.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    distanceTo: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            GeneralTitle(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start), text = title
            )

            Text(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                text = distanceTo,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption.copy(
                    color = MaterialTheme.colors.secondary
                )
            )
        }
        Text(
            text = subtitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.secondary
            )
        )
    }
}


@Composable
fun GeneralTitle(modifier: Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body2
    )
}

@Preview
@Composable
private fun Prev() {
    SearchItem(
        title = "The Mayor of Casterbridge Reading and annsdasdas",
        subtitle = "8502 Preston Rd. Inglewood, Maine 98380",
        distanceTo = "232 m"
    ) {}
}