package com.example.bhagwadgitachatbot.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bhagwadgitachatbot.LocationScreen
import com.example.bhagwadgitachatbot.screens.ChatScreen
import com.example.bhagwadgitachatbot.screens.LoginScreen
import com.example.homescreenbg.MainScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun RootNavGraph(
    navController: NavHostController,
    onSignInClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onSignInClick = onSignInClick
            )
        }
        composable("home") {
            MainScreen(navController)
        }
        composable("chat") {
            ChatScreen(navController)
        }
        composable("location") {
            LocationScreen(navController)
        }
    }
}