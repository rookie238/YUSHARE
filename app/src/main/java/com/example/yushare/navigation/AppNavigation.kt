package com.example.yushare.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yushare.R
import com.example.yushare.screens.CourseDetailScreen
import com.example.yushare.screens.HomeScreen
import com.example.yushare.screens.PlaceholderScreen
import com.example.yushare.screens.PostDetailScreen // <--- BU IMPORT ÖNEMLİ
import com.example.yushare.screens.UploadScreen
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun HomeWithNavBar() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // NavGraph fonksiyonunu çağırıyoruz
            NavGraph(navController, sharedViewModel)
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: SharedViewModel) {
    NavHost(navController = navController, startDestination = "home") {

        // 1. Ana Sayfa
        composable("home") { HomeScreen(viewModel, navController) }

        // 2. Yükleme Ekranı
        composable("upload") { UploadScreen(viewModel, navController) }

        // 3. Kurs Detay Ekranı (Parametre alıyor)
        composable(
            "courseDetail/{courseTitle}",
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: ""
            CourseDetailScreen(courseTitle, viewModel, navController)
        }

        // --- 4. EKLENEN KISIM: GÖNDERİ DETAY EKRANI ---
        // Yorum butonuna tıklayınca buraya gelecek
        composable("post_detail_placeholder") {
            PostDetailScreen(navController = navController)
        }
        // ----------------------------------------------

        // Diğer Alt Menü Ekranları
        composable("drafts") { PlaceholderScreen("Taslaklar") }
        composable("groups") { PlaceholderScreen("Gruplar") }
        composable("profile") { PlaceholderScreen("Profil") }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.home),
        BottomNavItem("drafts", R.drawable.student),
        BottomNavItem("upload", R.drawable.upload),
        BottomNavItem("groups", R.drawable.grup),
        BottomNavItem("profile", R.drawable.profile)
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
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.route,
                        modifier = Modifier.size(if (isSpecial) 32.dp else 26.dp),
                        tint = if (isSelected) Color.White else Color(0xFFAFAFAF)
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: Int)