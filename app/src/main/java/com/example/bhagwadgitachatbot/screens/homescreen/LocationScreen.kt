package com.example.bhagwadgitachatbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.bhagwadgitachatbot.ui.components.DynamicText
import com.example.bhagwadgitachatbot.ui.theme.customFont
import com.google.firebase.auth.FirebaseAuth
import com.example.bhagwadgitachatbot.viewmodel.SettingsViewModel
import com.example.bhagwadgitachatbot.viewmodel.SettingsViewModelFactory

//
//@Composable
//fun LocationScreen(navController: NavHostController) {
//    val viewModel: chatviewmodel = viewModel()
//    val auth = FirebaseAuth.getInstance()
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Background Galaxy GIF (same as HomeScreen)
//        Image(
//            painter = rememberImagePainter(
//                data = R.drawable.galaxy,
//                builder = {
//                    crossfade(true)
//                    placeholder(R.drawable.galaxy)
//                    error(R.drawable.galaxy)
//                }
//            ),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        // Sign Out Button
//        Button(
//            onClick = {
//                auth.signOut()
//                viewModel.resetState()
//                navController.navigate("login") {
//                    popUpTo("home") { inclusive = true }
//                }
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF3B5998)
//            ),
//            shape = RoundedCornerShape(8.dp),
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(16.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.ExitToApp,
//                contentDescription = "Sign Out",
//                tint = Color.White,
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("Sign Out", color = Color.White, fontFamily = customFont)
//        }
//
//        // Add your location-related content here
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                "Location Screen",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    fontFamily = customFont
//                ),
//                color = Color.White
//            )
//            // Add more location-related UI components here
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun LocationScreen(navController: NavHostController) {
    val viewModel: chatviewmodel = viewModel()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context.applicationContext)
    )

    // State for settings
    var isDarkMode by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16f) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Clear All Chats") },
            text = { Text("Are you sure you want to delete all chats? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.clearAllChats()
                        showConfirmDialog = false
                    }
                ) {
                    Text("Clear", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(R.drawable.galaxy)
                    .crossfade(true)
                    .decoderFactory { result, options, _ -> ImageDecoderDecoder(result.source, options) }
                    .build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Content with a semi-transparent background for better readability
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    "Settings",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = customFont
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Sign Out Button
                Button(
                    onClick = {
                        auth.signOut()
                        viewModel.resetState()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B5998)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sign Out", color = Color.White, fontFamily = customFont)
                }
            }

            // Settings content in a scrollable column with card backgrounds
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    SettingCard(
                        title = "Appearance",
                        icon = Icons.Default.DarkMode
                    ) {
                        // Theme toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Dark Mode",
                                color = Color.White,
                                fontFamily = customFont,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isDarkMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF3B5998),
                                    checkedTrackColor = Color(0xFF8B9DC3)
                                )
                            )
                        }

                        // Font size slider
                        Text(
                            "Font Size: ${fontSize.toInt()}sp",
                            color = Color.White,
                            fontFamily = customFont,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Slider(
                            value = fontSize,
                            onValueChange = { fontSize = it },
                            valueRange = 12f..24f,
                            steps = 6,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF3B5998),
                                activeTrackColor = Color(0xFF8B9DC3)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Language settings
                    SettingCard(
                        title = "Language",
                        icon = Icons.Default.Language
                    ) {
                        val languages = listOf("English", "Hindi", "Sanskrit")
                        Column {
                            languages.forEach { language ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedLanguage = language }
                                        .padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedLanguage == language,
                                        onClick = { selectedLanguage = language },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF3B5998)
                                        )
                                    )
                                    Text(
                                        language,
                                        color = Color.White,
                                        fontFamily = customFont,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Clear All Chats Card
                    SettingCard(
                        title = "Danger Zone",
                        icon = Icons.Default.Delete
                    ) {
                        Button(
                            onClick = { showConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935) // Red color
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Clear All Chats", color = Color.White)
                        }
                        
                        Text(
                            text = "This will permanently delete all your chat history",
                            color = Color.White.copy(alpha = 0.7f),
                            fontFamily = customFont,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // About section
                    SettingCard(
                        title = "About",
                        icon = Icons.Default.Info
                    ) {
                        Text(
                            "Bhagavad Gita Chatbot",
                            color = Color.White,
                            fontFamily = customFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Version 1.0.0",
                            color = Color.White,
                            fontFamily = customFont,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        DynamicText(
                            "This app provides spiritual guidance through AI-powered conversations based on the teachings of Bhagavad Gita.",
                            color = Color.White,
                            fontFamily = customFont,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x99000000) // Semi-transparent black
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                DynamicText(
                    title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = customFont
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            content()
        }
    }
}