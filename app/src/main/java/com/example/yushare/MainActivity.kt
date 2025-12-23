package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Ekran importlarınızın doğru olduğundan emin olun
import com.example.yushare.ui.screens.CreateGroupScreen
import com.example.yushare.ui.screens.GroupsScreen
import com.example.yushare.ui.screens.ChatScreen
import com.example.yushare.ui.screens.MenuScreen
import com.example.yushare.ui.screens.ProfileScreen
import com.example.yushare.ui.theme.YuBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Bu fonksiyon en başta çağrılmalı

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = YuBackground
                ) {
                    val navController = rememberNavController()

                    // startDestination: Uygulama açılınca ilk hangi ekran görünsün?
                    // "groups_screen" veya "profile_screen" yapabilirsiniz.
                    NavHost(navController = navController, startDestination = "groups_screen") {

                        // --- 1. GRUPLAR EKRANI ---
                        composable("groups_screen") {
                            GroupsScreen(
                                onNavigateToCreateGroup = {
                                    navController.navigate("create_group_screen")
                                },
                                onNavigateToChat = { groupName ->
                                    navController.navigate("chat_screen/$groupName")
                                },
                                // Örneğin profil butonuna basınca profile gitmesini isterseniz:
                                /* onNavigateToProfile = {
                                    navController.navigate("profile_screen")
                                }
                                */
                            )
                        }

                        // --- 2. GRUP KURMA EKRANI ---
                        composable("create_group_screen") {
                            CreateGroupScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToMessaging = {
                                    // Örnek akış: Grup kuruldu, ana sayfaya dön
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- 3. SOHBET (CHAT) EKRANI ---
                        composable(
                            route = "chat_screen/{groupName}",
                            arguments = listOf(navArgument("groupName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val groupName = backStackEntry.arguments?.getString("groupName") ?: "Study Group"
                            ChatScreen(
                                navController = navController, // Chat içinden geri dönmek gerekebilir
                                groupName = groupName
                            )
                        }

                        // --- 4. PROFİL EKRANI (Ekledik) ---
                        composable("profile_screen") {
                            ProfileScreen(
                                onMenuClick = {
                                    // Menüye git
                                    navController.navigate("menu_screen")
                                }
                            )
                        }

                        // --- 5. MENÜ EKRANI (Ekledik) ---
                        composable("menu_screen") {
                            MenuScreen(
                                onCloseClick = {
                                    // Geri gel (Profile döner)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}