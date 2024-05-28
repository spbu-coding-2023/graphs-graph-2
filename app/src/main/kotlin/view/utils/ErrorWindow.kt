package view.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ErrorWindow(message: String, onDismiss: () -> Unit) {
    val closeDialog = remember { mutableStateOf(false) }

    if (!closeDialog.value) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.size(300.dp, 180.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = 24.dp
            ) {
                Row(modifier = Modifier.height(100.dp).fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {
                        Text(text = "Error", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 32.dp)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(text = message)
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = { closeDialog.value = true }, modifier = Modifier.align(Alignment.End)) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
}



