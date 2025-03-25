package com.example.bhagwadgitachatbot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import coil.ImageLoader
import coil.decode.GifDecoder
import com.example.bhagwadgitachatbot.ui.theme.customFont
import com.example.bhagwadgitachatbot.database.ChatEntity
import com.example.bhagwadgitachatbot.database.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, chatViewModel: ChatViewModel) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val chats by chatViewModel.chatsState.collectAsState()

    // Create ImageLoader for GIF support
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    LaunchedEffect(auth.currentUser?.uid) {
        auth.currentUser?.uid?.let { userId ->
            // You can add any user-specific initialization here if needed
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    chatViewModel.createNewChat("New Conversation")
                    navController.navigate("chat")
                },
                containerColor = Color(0xFFFFD700),
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 100.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Galaxy GIF using AsyncImage
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.galaxy)
                    .build(),
                contentDescription = "Background GIF",
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Recent Chats",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = customFont
                    ),
                    fontSize = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn {
                    items(chats) { chat ->
                        ChatCard(chat = chat) {
                            navController.navigate("chat?chatId=${chat.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatCard(chat: ChatEntity, onClick: () -> Unit) {
    val galaxyGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF002147), // Deep Blue
            Color(0xFF008CFF), // Bright Blue
            Color(0xFFFFD700), // Golden Yellow
            Color(0xFFFF8C00)  // Orange
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(galaxyGradient)
                .padding(start = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bgremoved),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Chat ${chat.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = customFont
                )
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = customFont,
                    maxLines = 1
                )
            }
        }
    }
}
