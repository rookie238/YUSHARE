package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .graphicsLayer {
                                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                                clip = true
                            },
                        containerColor = Color(0xFF310078),
                        contentColor = Color.White
                    ) {
                        // 1. HOME
                        NavigationBarItem(
                            icon = { Icon(painterResource(id = R.drawable.icon_home), contentDescription = "Home") },
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
                            icon = { Icon(painterResource(id = R.drawable.icon_courses), contentDescription = "Courses") },
                            selected = currentRoute == "courses" || currentRoute?.startsWith("course_detail") == true,
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
                            icon = { Icon(painterResource(id = R.drawable.icon_groups), contentDescription = "Group") },
                            selected = false,
                            onClick = { /* Navigasyon eklenebilir */ },
                            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White.copy(alpha = 0.6f))
                        )

                        // 5. PROFİL
                        NavigationBarItem(
                            icon = { Icon(painterResource(id = R.drawable.icon_profile), contentDescription = "Profile") },
                            selected = false,
                            onClick = { /* Navigasyon eklenebilir */ },
                            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White.copy(alpha = 0.6f))
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
                        arguments = listOf(navArgument("courseId") { type = NavType.IntType })
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