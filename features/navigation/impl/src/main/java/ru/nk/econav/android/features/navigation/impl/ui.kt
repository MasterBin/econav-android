package ru.nk.econav.android.features.navigation.impl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.google.accompanist.insets.statusBarsPadding
import ru.nk.econav.android.data.routing.models.Instruction
import ru.nk.econav.core.common.decompose.OneChild

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    component: NavigationComponentImpl
) {
    val model by component.model.asState()

    Box(
        modifier = modifier.statusBarsPadding()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            InstructionContent(Modifier.padding(8.dp), model.instruction)
        }

        OneChild(state = component.userLocationComponent.state) {
            it.instance.render(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 16.dp)
            ).invoke()
        }
    }
}

@Composable
fun InstructionContent(
    modifier: Modifier = Modifier,
    instruction: Instruction
) {
    Row(
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            text = instruction.turnDescription,
            style = MaterialTheme.typography.h5
        )
        Column {
            Text(
                text = instruction.distance,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = instruction.time,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}