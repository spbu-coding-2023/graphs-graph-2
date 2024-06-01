package view.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import model.io.neo4j.Neo4jRepositoryHandler

@Composable
fun Neo4jLoginDialog(onDismiss: () -> Unit) {
    var uriInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isLoginSuccessful by remember { mutableStateOf(false) }

    val mainFontSize = 20.sp
    val secondaryFontSize = 15.sp

    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.background(Color.White).padding(16.dp).width(350.dp).height(360.dp)) {
            Text(text = "Please login to Neo4j AuraDB to use Neo4j:", fontSize = mainFontSize)

            val height = 60.dp

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = uriInput,
                onValueChange = {
                    uriInput = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth().height(height),
                label = {
                    Text(
                        "Uri",
                        style = MaterialTheme.typography.body1.copy(fontSize = secondaryFontSize),
                        color = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White))
            TextField(
                value = userInput,
                onValueChange = {
                    userInput = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth().height(height),
                label = {
                    Text(
                        "Name",
                        style = MaterialTheme.typography.body1.copy(fontSize = secondaryFontSize),
                        color = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White))
            TextField(
                value = passwordInput,
                onValueChange = {
                    passwordInput = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth().height(height),
                label = {
                    Text(
                        "Password",
                        style = MaterialTheme.typography.body1.copy(fontSize = secondaryFontSize),
                        color = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        modifier = Modifier.width(250.dp).height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                        onClick = {
                            if (Neo4jRepositoryHandler.initRepo(uriInput, userInput, passwordInput)) {
                                isLoginSuccessful = true
                            } else {
                                errorMessage = "Sorry, but this input is invalid."
                            }
                        }) {
                            Text("Login", color = Color.White)
                        }

                    if (errorMessage.isNotBlank()) {
                        Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))

                        uriInput = ""
                        userInput = ""
                        passwordInput = ""
                    }
                    if (isLoginSuccessful) {
                        Text(
                            text = "Logged in successfully!",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(top = 8.dp))
                        LaunchedEffect(Unit) {
                            delay(1500)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}
