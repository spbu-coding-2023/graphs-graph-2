package view.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodel.MainScreenViewModel

@Composable
fun <D> FAQBox(interactionSource: MutableInteractionSource, viewmodel: MainScreenViewModel<D>) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {

        val iconSize = 40.dp
        val paddingSize = 5.dp

        val textBoxHeight = 58.dp
        val textBoxWidth = 230.dp

        if (isHovered) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(3.dp, MaterialTheme.colors.secondary),
                modifier =
                Modifier
                    .height(textBoxHeight + paddingSize)
                    .width(textBoxWidth + paddingSize)
                    .padding(paddingSize)
            ) {
                Text(
                    text = viewmodel.graphViewModel.graphType.value.replace(" ", "\nData type: "),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Surface(
            color = Color.Transparent,
            modifier =
            Modifier
                .width(iconSize + paddingSize)
                .height(iconSize + paddingSize)
                .padding(paddingSize)
                .background(Color.Transparent)
                .hoverable(interactionSource = interactionSource)
        ) {
            if (!isHovered) {
                Image(
                    painterResource("drawable/question.svg"),
                    contentDescription = "Question Mark Icon",
                    modifier = Modifier.size(iconSize + paddingSize),
                    alignment = Alignment.TopEnd
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
