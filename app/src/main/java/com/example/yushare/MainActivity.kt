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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


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
                            arguments = listOf(navArgument("groupName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val groupName =
                                backStackEntry.arguments?.getString("groupName") ?: "Study Group"
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
                        setContent {
                            val navController = rememberNavController()
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            Scaffold(
                                bottomBar = {
                                    NavigationBar(
                                        modifier = Modifier
                                            .graphicsLayer {
                                                shape = RoundedCornerShape(
                                                    topStart = 30.dp,
                                                    topEnd = 30.dp
                                                )
                                                clip = true
                                            },
                                        containerColor = Color(0xFF310078),
                                        contentColor = Color.White
                                    ) {
                                        // 1. HOME
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painterResource(id = R.drawable.icon_home),
                                                    contentDescription = "Home"
                                                )
                                            },
                                            selected = currentRoute == "home",
                                            onClick = { navController.navigate("home") },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color(0xFF310078),
                                                indicatorColor = Color.White,
                                                unselectedIconColor = Color.White.copy(alpha = 0.6f)
                                            )
                                        )

                                        // 2. DERSLER
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painterResource(id = R.drawable.icon_courses),
                                                    contentDescription = "Courses"
                                                )
                                            },
                                            selected = currentRoute == "courses" || currentRoute?.startsWith(
                                                "course_detail"
                                            ) == true,
                                            onClick = { navController.navigate("courses") },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color(0xFF310078),
                                                indicatorColor = Color.White,
                                                unselectedIconColor = Color.White.copy(alpha = 0.6f)
                                            )
                                        )

                                        // 3. ARTI (+) İKONU
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_upload),
                                                    contentDescription = "Upload",
                                                    modifier = Modifier.size(32.dp)
                                                )
                                            },
                                            selected = currentRoute == "upload",
                                            onClick = { navController.navigate("upload") },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color(0xFF310078),
                                                indicatorColor = Color.White,
                                                unselectedIconColor = Color.White
                                            )
                                        )

                                        // 4. GRUP
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painterResource(id = R.drawable.icon_groups),
                                                    contentDescription = "Group"
                                                )
                                            },
                                            selected = false,
                                            onClick = { /* Navigasyon eklenebilir */ },
                                            colors = NavigationBarItemDefaults.colors(
                                                unselectedIconColor = Color.White.copy(alpha = 0.6f)
                                            )
                                        )

                                        // 5. PROFİL
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    painterResource(id = R.drawable.icon_profile),
                                                    contentDescription = "ProfileScreen"
                                                )
                                            },
                                            selected = false,
                                            onClick = { /* Navigasyon eklenebilir */ },
                                            colors = NavigationBarItemDefaults.colors(
                                                unselectedIconColor = Color.White.copy(alpha = 0.6f)
                                            )
                                        )
                                    }
                                }
                            ) { innerPadding ->
                                NavHost(
                                    navController = navController,
                                    startDestination = "home",
                                    modifier = Modifier.padding(innerPadding)
                                ) {
                                    composable("home") { HomePage() }

                                    // DERS LİSTESİ - HATA BURADAYDI
                                    composable("courses") {
                                        CourseListScreen(
                                            onCourseClick = { courseId ->
                                                navController.navigate("course_detail/$courseId")
                                            },
                                            // EKSİK PARAMETRE EKLENDİ:
                                            onBackClick = {
                                                navController.popBackStack()
                                            }
                                        )
                                    }

                                    // YÜKLEME EKRANI
                                    composable("upload") {
                                        ShareScreen(onBackClick = { navController.popBackStack() })
                                    }

                                    // DERS DETAY EKRANI
                                    composable(
                                        route = "course_detail/{courseId}",
                                        arguments = listOf(navArgument("courseId") {
                                            type = NavType.IntType
                                        })
                                    ) { backStackEntry ->
                                        val id = backStackEntry.arguments?.getInt("courseId") ?: 0
                                        CourseDetailScreen(
                                            courseId = id,
                                            onBackClick = { navController.popBackStack() }
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}