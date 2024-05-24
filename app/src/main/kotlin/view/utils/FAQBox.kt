package view.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import viewmodel.MainScreenViewModel

@Composable
fun <D>FAQBox(interactionSource: MutableInteractionSource, viewmodel: MainScreenViewModel<D>) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {

        Surface(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(220.dp).height(50.dp)
                .background(if (isHovered) Color.LightGray else Color.Gray)
                .hoverable(interactionSource = interactionSource)
        ) {
            if (isHovered) {
                Box(
                    modifier = Modifier.width(200.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = viewmodel.graphViewModel.graphType.value.replace(" ", "\nData type: "),
                        color = Color.Black
                    )
                }
            } else {
                Image(
                    painterResource("drawable/question.png"),
                    contentDescription = "Question Mark Icon",
                    modifier = Modifier.size(10.dp).padding(start = 50.dp)
                )
            }
        }

        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> {
                        isHovered = true
                    }

                    is HoverInteraction.Exit -> {
                        isHovered = false
                    }
                }
            }
        }
    }
}
