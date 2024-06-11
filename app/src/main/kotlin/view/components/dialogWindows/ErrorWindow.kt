package view.components.dialogWindows

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ErrorWindow(message: String, onDismiss: () -> Unit) {
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
                    Button(onClick = { onDismiss() }, modifier = Modifier.align(Alignment.End).width(100.dp)) {
                        Text("ok")
                    }
                }
            }
        }
    }
}
