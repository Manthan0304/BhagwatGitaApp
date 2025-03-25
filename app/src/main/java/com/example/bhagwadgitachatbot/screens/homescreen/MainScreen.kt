package com.example.homescreenbg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bhagwadgitachatbot.BottomBar
import com.example.bhagwadgitachatbot.MainNavGraph
import com.example.bhagwadgitachatbot.HomeScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(rootNavController: NavHostController) {
    val isImeVisible = WindowInsets.isImeVisible
    val bottomNavBarBgColor = Color.Black
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bottomNavBarBgColor)
            ) {
                BottomBar(navController = mainNavController)
            }
        },
        modifier = Modifier
            .background(color = bottomNavBarBgColor)
            .systemBarsPadding()
    ) { innerPadding ->
        val contentPadding = remember(isImeVisible) {
            if (isImeVisible)
                PaddingValues(bottom = 56.dp)
            else
                PaddingValues(bottom = 0.dp)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            MainNavGraph(
                navController = mainNavController,
                rootNavController = rootNavController
            )
        }
    }
}