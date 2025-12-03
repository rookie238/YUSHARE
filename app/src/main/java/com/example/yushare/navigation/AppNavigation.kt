package com.example.yushare.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yushare.Screens.CourseDetailScreen
import com.example.yushare.Screens.PlaceholderScreen
import com.example.yushare.Screens.UploadScreen
import com.example.yushare.Screens.HomeScreen
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun HomeWithNavBar() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(navController, sharedViewModel)
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: SharedViewModel) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(viewModel, navController) }
        composable("upload") { UploadScreen(viewModel, navController) }

        composable(
            "courseDetail/{courseTitle}",
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: ""
            CourseDetailScreen(courseTitle, viewModel, navController)
        }

        composable("drafts") { PlaceholderScreen("Taslaklar") }
        composable("groups") { PlaceholderScreen("Gruplar") }
        composable("profile") { PlaceholderScreen("Profil") }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home),
        BottomNavItem("drafts", Icons.Default.Edit),
        BottomNavItem("upload", Icons.Default.AddCircle),
        // Eğer Group ikonu hata verirse yerine Person kullandık,
        // kütüphaneyi eklediysen Icons.Default.Group yapabilirsin.
        BottomNavItem("groups", Icons.Default.Person),
        BottomNavItem("profile", Icons.Default.Person)
    )
    NavigationBar(containerColor = Color(0xFF2B0B5E), contentColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val isSpecial = item.route == "upload"
            NavigationBarItem(
                selected = isSelected,
                onClick = { navController.navigate(item.route) { popUpTo("home"); launchSingleTop = true } },
                icon = { Icon(imageVector = item.icon, contentDescription = item.route, modifier = Modifier.size(if (isSpecial) 32.dp else 26.dp), tint = if (isSelected) Color.White else Color(0xFFAFAFAF)) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector)