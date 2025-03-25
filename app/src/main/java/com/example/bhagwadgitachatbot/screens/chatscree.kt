package com.example.bhagwadgitachatbot.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bhagwadgitachatbot.api.ChatRequest
import com.example.bhagwadgitachatbot.api.RetrofitInstance
import kotlinx.coroutines.launch

private const val TAG = "ChatScreen"

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val id: String = java.util.UUID.randomUUID().toString() // Add unique ID for each message
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {
    var userInput by remember { mutableStateOf("") }
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Add initial message
    LaunchedEffect(Unit) {
        if (chatMessages.isEmpty()) {
            chatMessages.add(
                ChatMessage(
                    "Hello! I'm your Bhagavad Gita assistant. How can I help you today?",
                    isUser = false
                )
            )
        }
    }

    // Scroll to bottom when new message is added
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bhagavad Gita Chatbot",
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
                ),
                modifier = Modifier.systemBarsPadding() // Add this line to keep TopAppBar fixed
                     .windowInsetsPadding(WindowInsets.systemBars) // Add this line
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(paddingValues)
                .imePadding() // Add this modifier

        ) {
            // Chat messages
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    state = listState,
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = chatMessages,
                        key = { it.id } // Use unique ID as key for better recomposition
                    ) { message ->
                        ChatMessageItem(message = message)
                    }
                }

                // Loading indicator overlay
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2E2E2E)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color(0xFFFFD700),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Krishna is typing...", color = Color.White)
                            }
                        }
                    }
                }
            }

            // Message input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text("Ask Bhagavad Gita...", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF2E2E2E),
                        unfocusedContainerColor = Color(0xFF2E2E2E),
                        focusedIndicatorColor = Color(0xFFFFD700),
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (userInput.isNotBlank() && !isLoading) {
                                val currentInput = userInput
                                userInput = ""
                                isLoading = true

                                Log.d(TAG, "Sending message: $currentInput")
                                chatMessages.add(ChatMessage(currentInput, true))

                                coroutineScope.launch {
                                    try {
                                        Log.d(TAG, "Making API request with query: $currentInput")
                                        val response = RetrofitInstance.api.getChatResponse(
                                            ChatRequest(
                                                query = currentInput,
                                                num_verses = 3
                                            )
                                        )

                                        Log.d(TAG, "API response received: ${response.isSuccessful}")
                                        if (response.isSuccessful) {
                                            val chatResponse = response.body()
                                            Log.d(TAG, "Response body: $chatResponse")

                                            if (chatResponse != null) {
                                                val formattedResponse = buildString {
                                                    append(chatResponse.response)
                                                    append("\n\nRelevant Verses:\n")
                                                    chatResponse.relevant_verses.forEach { verse ->
                                                        append("\n${verse.verse_number}:\n${verse.translation_in_english}\n")
                                                    }
                                                }
                                                chatMessages.add(ChatMessage(formattedResponse, false))
                                            } else {
                                                Log.e(TAG, "Response body is null")
                                                chatMessages.add(ChatMessage("Sorry, I couldn't process that response.", false))
                                            }
                                        } else {
                                            Log.e(TAG, "API request failed: ${response.code()} - ${response.message()}")
                                            chatMessages.add(ChatMessage("Sorry, there was an error processing your request. Please try again.", false))
                                        }
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Exception during API call", e)
                                        chatMessages.add(ChatMessage("Sorry, there was a network error. Please check your connection and try again.", false))
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.size(8.dp))

                IconButton(
                    onClick = {
                        if (userInput.isNotBlank() && !isLoading) {
                            val currentInput = userInput
                            userInput = ""
                            isLoading = true

                            Log.d(TAG, "Sending message: $currentInput")
                            chatMessages.add(ChatMessage(currentInput, true))

                            coroutineScope.launch {
                                try {
                                    Log.d(TAG, "Making API request with query: $currentInput")
                                    val response = RetrofitInstance.api.getChatResponse(
                                        ChatRequest(
                                            query = currentInput,
                                            num_verses = 3
                                        )
                                    )

                                    Log.d(TAG, "API response received: ${response.isSuccessful}")
                                    if (response.isSuccessful) {
                                        val chatResponse = response.body()
                                        Log.d(TAG, "Response body: $chatResponse")

                                        if (chatResponse != null) {
                                            val formattedResponse = buildString {
                                                append(chatResponse.response)
                                                append("\n\nRelevant Verses:\n")
                                                chatResponse.relevant_verses.forEach { verse ->
                                                    append("\n${verse.verse_number}:\n${verse.translation_in_english}\n")
                                                }
                                            }
                                            chatMessages.add(ChatMessage(formattedResponse, false))
                                        } else {
                                            Log.e(TAG, "Response body is null")
                                            chatMessages.add(ChatMessage("Sorry, I couldn't process that response.", false))
                                        }
                                    } else {
                                        Log.e(TAG, "API request failed: ${response.code()} - ${response.message()}")
                                        chatMessages.add(ChatMessage("Sorry, there was an error processing your request. Please try again.", false))
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Exception during API call", e)
                                    chatMessages.add(ChatMessage("Sorry, there was a network error. Please check your connection and try again.", false))
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(0xFFFFD700),
                            shape = CircleShape
                        ),
                    enabled = !isLoading && userInput.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(animationSpec = tween(300)) { it / 2 },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(
                    topStart = if (message.isUser) 16.dp else 0.dp,
                    topEnd = if (message.isUser) 0.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isUser)
                        Color(0xFFFFD700)
                    else
                        Color(0xFF2E2E2E)
                )
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = if (message.isUser)
                        Color.Black
                    else
                        Color.White
                )
            }
        }
    }
}