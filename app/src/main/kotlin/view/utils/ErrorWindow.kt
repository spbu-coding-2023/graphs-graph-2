package view.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ErrorWindow(message: String, onDismiss: () -> Unit) {
    Dialog(onCloseRequest = onDismiss) {
        Surface(
            modifier = Modifier.size(300.dp, 150.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = 24.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Error", style = MaterialTheme.typography.h6)
                Text(text = message)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDismiss) {
                    Text("Exit")
                }
            }
        }
    }
}
