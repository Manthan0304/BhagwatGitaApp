package com.example.bhagwadgitachatbot.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
        composable(
            route = "login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            LoginScreen(
                onSignInClick = onSignInClick
            )
        }
        
        composable(
            route = "home",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            MainScreen(navController)
        }
        
        composable(
            route = "chat?chatId={chatId}",
            arguments = listOf(
                navArgument("chatId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            enterTransition = {
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(
                    targetScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId")
            ChatScreen(
                navController = navController,
                chatId = if (chatId == -1) null else chatId
            )
        }
        
        composable(
            route = "location",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            LocationScreen(navController)
        }
    }
}