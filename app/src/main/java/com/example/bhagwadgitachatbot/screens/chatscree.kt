package com.example.bhagwadgitachatbot.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.animation.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var userInput by remember { mutableStateOf("") }
    val chatMessages = remember { mutableStateListOf<Pair<String, Boolean>>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isSidebarVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF121212))))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            AnimatedVisibility(
                visible = isSidebarVisible,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(250.dp)
                        .background(Color(0xFF2E2E2E))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "Menu",
                            color = Color(0xFFFFD700),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Add menu items here
                        MenuButton("Previous Chats")
                        MenuButton("Settings")
                        MenuButton("About")
                        MenuButton("Help")
                    }
                }
            }

            // Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top bar with menu button and title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isSidebarVisible = !isSidebarVisible }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFFFFD700)
                            )
                        }

                        Text(
                            text = "Bhagavad Gita Chatbot",
                            fontSize = 22.sp,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        reverseLayout = true
                    ) {
                        items(chatMessages.reversed()) { message ->
                            AnimatedVisibility(visible = true, enter = slideInVertically()) {
                                ChatBubble(message.first, message.second)
                            }
                        }
                    }

                    if (isLoading) {
                        Text(
                            text = "AI is thinking...",
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color(0xFF2E2E2E), RoundedCornerShape(16.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Ask Bhagavad Gita...", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFFFFD700),
                                unfocusedIndicatorColor = Color.Gray
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    sendMessage(
                                        userInput,
                                        chatMessages,
                                        coroutineScope,
                                        { isLoading = it },
                                        { userInput = "" }
                                    )
                                }
                            )
                        )

                        IconButton(
                            onClick = {
                                sendMessage(
                                    userInput,
                                    chatMessages,
                                    coroutineScope,
                                    { isLoading = it },
                                    { userInput = "" }
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(text: String) {
    Button(
        onClick = { /* Handle menu item click */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

fun sendMessage(
    userInput: String,
    chatMessages: MutableList<Pair<String, Boolean>>,
    coroutineScope: CoroutineScope,
    setLoading: (Boolean) -> Unit,
    clearInput: () -> Unit
) {
    if (userInput.isNotBlank()) {
        chatMessages.add(userInput to true)
        clearInput()

        coroutineScope.launch {
            setLoading(true)
            val aiReply = fetchAIResponse(userInput)
            setLoading(false)
            chatMessages.add(aiReply to false)
        }
    }
}

suspend fun fetchAIResponse(userMessage: String): String {
    delay(1200) // Simulate delay
    return "AI: Response to \"$userMessage\"" // Replace with actual API call
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFF3B5998) else Color(0xFFFFD700),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = if (isUser) Color.White else Color.Black,
                fontSize = 16.sp
            )
        }
    }
}