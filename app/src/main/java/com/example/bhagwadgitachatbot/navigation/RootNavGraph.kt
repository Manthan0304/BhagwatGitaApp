package com.example.bhagwadgitachatbot.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        composable(
            route = "chat?chatId={chatId}",
            arguments = listOf(
                navArgument("chatId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId")
            ChatScreen(
                navController = navController,
                chatId = if (chatId == -1) null else chatId
            )
        }
        composable("location") {
            LocationScreen(navController)
        }
    }
}