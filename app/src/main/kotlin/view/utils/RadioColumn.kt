package view.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RadioColumn(
    selectText: String,
    currentDataIndex: MutableState<Int>,
    radioOptions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(selectText)
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            radioOptions.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .clickable { currentDataIndex.value = index }
                ) {
                    RadioButton(
                        selected = currentDataIndex.value == index,
                        onClick = { currentDataIndex.value = index },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.secondary
                        )
                    )
                    Text(
                        text = option,
                        style = TextStyle(fontSize = 16.sp),
                        color = if (currentDataIndex.value == index) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}
