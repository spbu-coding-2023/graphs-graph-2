package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.RadioButton
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CreateGraphDialogWindow() {

    var closeDialog by remember { mutableStateOf(false) }

    if (!closeDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier =
                    Modifier.background(Color.White).padding(16.dp).width(700.dp).height(390.dp)
            ) {
                Text(
                    "Create Graph",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                        Text("Select stored data:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        val radioOptions = listOf("Integer", "UInteger", "String", "Boolean")
                        val selectedOptionIndex = remember { mutableStateOf(0) }
                        Column(modifier = Modifier.width(220.dp)) {
                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                            selectedOptionIndex.value = index
                                        }
                                ) {
                                    RadioButton(
                                        selected = selectedOptionIndex.value == index,
                                        onClick = { selectedOptionIndex.value = index },
                                        colors =
                                            RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colors.secondary
                                            )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                            if (selectedOptionIndex.value == index) Color.Black
                                            else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                        Text("Select the orientation:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        val radioOptions = listOf("Undirected", "Directed")
                        val selectedOptionIndex = remember { mutableStateOf(0) }
                        Column(modifier = Modifier.width(220.dp)) {
                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                            selectedOptionIndex.value = index
                                        }
                                ) {
                                    RadioButton(
                                        selected = selectedOptionIndex.value == index,
                                        onClick = { selectedOptionIndex.value = index },
                                        colors =
                                            RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colors.secondary
                                            )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                            if (selectedOptionIndex.value == index) Color.Black
                                            else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.width(250.dp).height(200.dp)) {
                        Text("Select the weightnes*:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        Column(modifier = Modifier.width(220.dp)) {
                            val radioOptions = listOf("Unweighted", "Weighted")
                            val selectedOptionIndex = remember { mutableStateOf(0) }

                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                            selectedOptionIndex.value = index
                                        }
                                ) {
                                    RadioButton(
                                        selected = selectedOptionIndex.value == index,
                                        onClick = { selectedOptionIndex.value = index },
                                        colors =
                                            RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colors.secondary
                                            )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                            if (selectedOptionIndex.value == index) Color.Black
                                            else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.width(145.dp).height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                        onClick = { closeDialog = true }
                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            }
        }
    }
}
