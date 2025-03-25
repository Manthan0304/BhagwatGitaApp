package com.example.bhagwadgitachatbot.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestChatScreen(navController: NavHostController) {
    var msg by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Pair<String, Boolean>>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bhagavad Gita Chatbottt",
                        color = Color(0xFFFFD700),
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFFD700)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF121212))))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Messages area
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = false
                    ) {
                        items(messages) { message ->
                            ChatBubble(message.first, message.second)
                        }
                    }

                    if (isLoading) {
                        Text(
                            text = "AI is thinking...",
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
                    }
                }

                // Input field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2E2E2E), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = msg,
                        onValueChange = { msg = it },
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
                                if (msg.isNotBlank()) {
                                    messages.add(msg to true)
                                    val userMessage = msg
                                    msg = ""
                                    coroutineScope.launch {
                                        isLoading = true
                                        val response = fetchTestAIResponse(userMessage)
                                        isLoading = false
                                        messages.add(response to false)
                                    }
                                }
                            }
                        )
                    )

                    IconButton(
                        onClick = {
                            if (msg.isNotBlank()) {
                                messages.add(msg to true)
                                val userMessage = msg
                                msg = ""
                                coroutineScope.launch {
                                    isLoading = true
                                    val response = fetchTestAIResponse(userMessage)
                                    isLoading = false
                                    messages.add(response to false)
                                }
                            }
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

private suspend fun fetchTestAIResponse(userMessage: String): String {
    kotlinx.coroutines.delay(1000) // Simulate API call
    return "This is a test response to: $userMessage"
}
