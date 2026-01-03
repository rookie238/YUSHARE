package com.example.yushare.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Bu import çok önemli
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yushare.R
// Ekran Importları (Paket isimlerinize dikkat edin)
import com.example.yushare.screens.CourseDetailScreen
import com.example.yushare.screens.CoursesScreen
import com.example.yushare.screens.GroupsScreen
import com.example.yushare.screens.HomeScreen
import com.example.yushare.screens.PlaceholderScreen
import com.example.yushare.screens.PostDetailScreen
import com.example.yushare.screens.UploadScreen
import com.example.yushare.screens.ProfileScreen
import com.example.yushare.screens.MenuScreen
// ViewModel Importları
import com.example.yushare.viewmodel.SharedViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.yushare.screens.ForgotPasswordScreen
import com.example.yushare.screens.NotificationsScreen
import com.example.yushare.screens.CreateGroupScreen
import com.example.yushare.screens.GroupChatScreen


@Composable
fun HomeWithNavBar() {
    val navController = rememberNavController()
    // Genel uygulama verileri için ViewModel
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
fun NavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {


    val viewModel: com.example.yushare.viewmodel.SharedViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    NavHost(navController = navController, startDestination = "home") {

        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController)
        }

        // 1. Ana Sayfa
        composable("home") { HomeScreen(sharedViewModel, navController) }

        // 2. Yükleme Ekranı
        composable("upload") { UploadScreen(sharedViewModel, navController) }

        // 3. Kurs Detay Ekranı
        composable(
            "courseDetail/{courseTitle}",
            arguments = listOf(navArgument("courseTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: ""
            CourseDetailScreen(courseTitle, sharedViewModel, navController)
        }

        // 4. Gönderi Detay
        composable(
            "post_detail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Linkten gelen postId'yi alıyoruz
            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            // Ekranı çağırırken ID ve ViewModel'i gönderiyoruz
            PostDetailScreen(navController = navController, postId = postId, viewModel = sharedViewModel)
        }

        // Diğer Alt Menü Ekranları
        composable("drafts") {
            // Taslaklar butonuna basınca Tüm Dersler ekranı gelsin
            // (Önceki adımda oluşturduğumuz CoursesScreen dosyasını import ettiğinden emin ol)
            CoursesScreen(viewModel = sharedViewModel, navController = navController)
        }
        // --- GROUPS SCREEN GÜNCELLEMESİ ---
        composable("groups") {
            GroupsScreen(
                onNavigateToCreateGroup = { navController.navigate("create_group") },
                onNavigateToChat = { groupId, groupName ->
                    navController.navigate("chat/$groupId/$groupName")
                },
                // YENİ EKLENEN PARAMETRELER:
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                }
            )
        }

        // CreateGroupScreen çağrısı:
        composable("create_group") {
            CreateGroupScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMessaging = { groupId, groupName ->
                    // Grup oluşunca direkt sohbete atla, geri tuşuyla Groups'a dönsün diye popUp yapıyoruz
                    navController.navigate("chat/$groupId/$groupName") {
                        popUpTo("groups")
                    }
                }
            )
        }

        // ChatScreen çağrısı (Parametreleri alıyor):
        // AppNavigation.kt içindeki NavGraph fonksiyonunda:

        composable(
            route = "chat/{groupId}/{groupName}",
            arguments = listOf(
                navArgument("groupId") { type = NavType.StringType },
                navArgument("groupName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            val groupName = backStackEntry.arguments?.getString("groupName") ?: "Chat"

            GroupChatScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                // YENİ EKLENEN KISIM:
                onNavigateToNotifications = {
                    navController.navigate("notifications")
                },
                groupId = groupId,
                groupName = groupName
            )
        }

        composable("profile") {
            ProfileScreen(sharedViewModel, navController)
        }

        composable("menu") {
            MenuScreen(
                viewModel = sharedViewModel,
                navController = navController
            )
        }

        // --- AppNavigation.kt içine eklenecek ---

        composable("notifications") {
            // Birazdan oluşturacağımız ekranı buraya bağlıyoruz
            NotificationsScreen(
                navController = navController
            )
        }
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
    NavigationBar(containerColor = Color(0xFF294BA3), contentColor = Color.White,
        modifier = Modifier.height(55.dp)) {
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

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    // Preview ortamında sahte bir NavController oluşturuyoruz
    val navController = rememberNavController()

    // BottomNavBar'ı çağırıyoruz
    BottomNavBar(navController = navController)
}