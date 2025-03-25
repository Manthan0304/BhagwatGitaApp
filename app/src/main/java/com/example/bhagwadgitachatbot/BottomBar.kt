import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bhagwadgitachatbot.R
import com.example.bhagwadgitachatbot.ui.theme.customFont

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNav.Screen1,
        BottomNav.Screen2
    )

    BottomNavigation(
        backgroundColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = {
                    Text(
                        screen.title,
                        fontFamily = customFont
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color(0xFFFFD700),
                unselectedContentColor = Color.White
            )
        }
    }
}

sealed class BottomNav(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Screen1 : BottomNav(
        route = "home_screen",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Screen2 : BottomNav(
        route = "location_screen",
        title = "Location",
        icon = Icons.Default.LocationOn
    )
} 