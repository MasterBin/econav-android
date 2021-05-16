package ru.nk.econav.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.nk.econav.ui.R

@Composable
fun EcoParamSlider(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.ecology_of_route),
    value: Float,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Surface(
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onValueChange(valueRange.start)
                    onValueChangeFinished()
                }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_chronometer),
                        contentDescription = ""
                    )
                }
                Slider(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = onValueChange,
                    onValueChangeFinished = onValueChangeFinished,
                    valueRange = valueRange,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colors.primaryVariant,
                        thumbColor = MaterialTheme.colors.primaryVariant,
                        inactiveTrackColor = MaterialTheme.colors.secondary
                    )
                )
                IconButton(onClick = {
                    onValueChange(valueRange.endInclusive)
                    onValueChangeFinished()
                }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_two_leaves),
                        contentDescription = ""
                    )
                }
            }
        }
    }

}