package com.example.bhagwadgitachatbot.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.bhagwadgitachatbot.R
import com.example.bhagwadgitachatbot.services.MusicService

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    onSignInClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val musicService = remember { MusicService.getInstance(context) }

    // Start music when the screen is composed
    LaunchedEffect(Unit) {
        musicService.startMusic()
    }

    // Cleanup when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            musicService.stopMusic()
        }
    }

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory()) // Alternative GIF decoder
        }
        .build()

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.background) // Ensure GIF is in `res/drawable`
                .build(),
            contentDescription = "Background GIF",
            imageLoader = imageLoader,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { 
                    musicService.stopMusic()
                    onSignInClick() 
                },
                modifier = Modifier
                    .width(250.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Start Chat",
                    color = Color(0xFF1A237E),
                    fontSize = 20.sp,
                       fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}



@Composable
private fun DecorativeCorners() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left corner
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
                .align(Alignment.TopStart) // Fixed here
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginimage),
                contentDescription = "Corner decoration",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top-right corner
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
                .align(Alignment.TopEnd) // Fixed here
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginimage),
                contentDescription = "Corner decoration",
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(90f)
            )
        }

        // Bottom-left corner
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
                .align(Alignment.BottomStart) // Fixed here
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginimage),
                contentDescription = "Corner decoration",
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(270f)
            )
        }

        // Bottom-right corner
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp)
                .align(Alignment.BottomEnd) // Fixed here
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginimage),
                contentDescription = "Corner decoration",
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(180f)
            )
        }
    }
}