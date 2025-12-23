package com.example.yushare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yushare.ui.screens.CreateGroupScreen // Ekranlarını import et
import com.example.yushare.ui.screens.GroupsScreen    // Ekranlarını import et
import com.example.yushare.ui.theme.YuBackground// Arka plan rengini tema dosyasından alıyoruz
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.yushare.ui.screens.ChatScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Eğer projenizde YUSHARETheme adında bir tema dosyanız varsa burayı
            // YUSHARETheme { ... } olarak değiştirebilirsiniz.
            // Şimdilik standart MaterialTheme kullanıyoruz.
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = YuBackground // Tasarımımızdaki Bej Rengi
                ) {
                    // Ana navigasyon kontrolcüsü
                    val navController = rememberNavController()

                    // Başlangıç noktası olarak Gruplar ekranını belirliyoruz
                    NavHost(navController = navController, startDestination = "groups_screen") {

                        // 1. GROUPS SCREEN (Ana Liste)
                        composable("groups_screen") {
                            GroupsScreen(
                                onNavigateToCreateGroup = {
                                    // Create butonuna basılınca Grup Kurma ekranına git
                                    navController.navigate("create_group_screen")
                                },
                                        onNavigateToChat = { groupName ->
                                    // Gruba tıklanınca ismini taşıyarak Chat ekranına git
                                    navController.navigate("chat_screen/$groupName")
                                }
                            )
                        }

                        // 2. CREATE GROUP SCREEN (Grup Kurma)
                        composable("create_group_screen") {
                            CreateGroupScreen(
                                onNavigateBack = {
                                    // Geri okuna basılınca önceki sayfaya dön
                                    navController.popBackStack()
                                },
                                onNavigateToMessaging = {
                                    // Next butonuna basılınca Mesajlaşma ekranına git
                                    // popUpTo ile geri gelince tekrar form ekranını görmemesini sağlıyoruz
                                    navController.navigate("messaging_screen_placeholder") {
                                        popUpTo("groups_screen") { inclusive = false }
                                    }
                                }
                            )
                        }
                        composable(
                            route = "chat_screen/{groupName}", // {groupName} dinamik parametre
                            arguments = listOf(navArgument("groupName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Gelen ismi al
                            val groupName = backStackEntry.arguments?.getString("groupName") ?: "Study Group"

                            ChatScreen(
                                navController = navController,
                                groupName = groupName
                            )
                        }

                        }
                    }
                }
            }
        }
    }
