package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.tabScreen.analyzeTab.borderPadding
import view.tabScreen.analyzeTab.horizontalGap
import view.tabScreen.analyzeTab.rowHeight
import viewmodel.graph.GraphViewModel


@Composable
fun <D> LayoutUI(graphVM: GraphViewModel<D>) {
    var sliderValue1 by remember { mutableStateOf(0f) }
    var sliderValue2 by remember { mutableStateOf(0f) }
    var sliderValue3 by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Analyze Tab", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 16.dp))

        SliderWithValue(
            value = sliderValue1,
            onValueChange = { sliderValue1 = it },
            label = "Slider 1",
            valueRange = 0f..2f,
            coefficient = 1f
        )

        Spacer(modifier = Modifier.height(16.dp))

        SliderWithValue(
            value = sliderValue2,
            onValueChange = { sliderValue2 = it },
            label = "Slider 2",
            valueRange = 1f..30f,
            coefficient = 1f
        )

        Spacer(modifier = Modifier.height(16.dp))

        SliderWithValue(
            value = sliderValue3,
            onValueChange = { sliderValue3 = it },
            label = "Slider 3",
            valueRange = 0.5f..10f,
            coefficient = 1f
        )
        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            onClick = {},
//            onClick = { graphVM.applyForceDirectedLayout(740.0, 650.0, sliderValue1.toDouble(), sliderValue2.toDouble(), sliderValue3.toDouble())})
        ) {
            Text("Apply layout settings")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            onClick = {}
//            onClick = { graphVM.randomize(740.0, 650.0) }) {
        ) {
            Text("Randomize")
        }

    }
}

@Composable
fun SliderWithValue(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    valueRange: ClosedFloatingPointRange<Float>,
    coefficient: Float
) {
    Column {
        Text("$label: ${"%.2f".format(value * coefficient)}", style = MaterialTheme.typography.body1)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) / 1000).toInt(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}