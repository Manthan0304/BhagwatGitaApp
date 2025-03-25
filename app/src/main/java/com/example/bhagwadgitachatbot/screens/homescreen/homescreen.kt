package com.example.bhagwadgitachatbot

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import coil.ImageLoader
import coil.decode.GifDecoder
import com.example.bhagwadgitachatbot.ui.theme.customFont

@Composable
fun HomeScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val viewModel: chatviewmodel = viewModel()
    var username by remember { mutableStateOf("User") }
    val context = LocalContext.current

    // Create ImageLoader for GIF support
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    // Fetch username from Firestore using viewModel state
    val userData by viewModel.state.collectAsState()

    LaunchedEffect(auth.currentUser?.uid) {
        auth.currentUser?.uid?.let { userId ->
            viewModel.getUserData(userId)
        }
    }

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

            val recentChats = listOf("Test Chat", "Chat 2", "Chat 3", "Chat 4")
            LazyColumn {
                items(recentChats) { chatName ->
                    ChatCard(chatName = chatName) {
                        if (chatName == "Test Chat") {
                            navController.navigate("test_chat")
                        } else {
                            navController.navigate("chat")
                        }
                    }
                }
            }
        }
    }
}

fun fetchUsernameFromFirebase(onResult: (String) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            val name = document.getString("username") ?: "User"
            onResult(name) // Update UI
        }
        .addOnFailureListener {
            onResult("User") // Handle error
        }
}


@Composable
fun ChatCard(chatName: String, onClick: () -> Unit) {
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
        border = BorderStroke(2.dp, Color.White) // Adds white outline
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(galaxyGradient) // Apply Gradient Background
                .padding(start = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bgremoved), // Your Image
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize() // Ensures the image fills the Box
                    .alpha(0.3f), // Adjust Transparency if Needed
                contentScale = ContentScale.Crop // Stretches the image to fit
            )

            Text(
                text = chatName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = customFont,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Home", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Chat", tint = Color.White) },
            label = { Text("Chat", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White) },
            label = { Text("Settings", color = Color.White) }
        )
    }
}
