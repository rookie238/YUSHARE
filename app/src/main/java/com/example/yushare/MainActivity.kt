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

import androidx.compose.ui.graphics.graphicsLayer // Şekil vermek için

import androidx.compose.ui.draw.clip // Kırpmak için

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

import androidx.navigation.NavGraph.Companion.findStartDestination

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



// Hangi sayfada olduğumuzu takip etmek için:

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route



            Scaffold(

// Alt Menü

                bottomBar = {

// Alt menünün şekli

                    NavigationBar(

                        modifier = Modifier

                            .graphicsLayer {

                                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)

                                clip = true

                            },

                        containerColor = Color(0xFF310078), // Tasarımdaki Mor Renk

                        contentColor = Color.White

                    ) {

// 1. HOME İKONU

                        NavigationBarItem(

                            icon = { Icon(painterResource(id = R.drawable.icon_home), contentDescription = "Home") },

                            selected = currentRoute == "home",

                            onClick = { navController.navigate("home") },

                            colors = NavigationBarItemDefaults.colors(

                                selectedIconColor = Color(0xFF310078), // Seçilince ikon mor olsun

                                selectedTextColor = Color.White,

                                indicatorColor = Color.White, // Seçilince arkasındaki yuvarlak beyaz olsun

                                unselectedIconColor = Color.White.copy(alpha = 0.6f)

                            )

                        )



// 2. DERSLER İKONU

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

// Artı ikonunu daire içine alıyor
                                Icon(

                                    painter = painterResource(id = R.drawable.icon_upload), // icon_plus.svg eklediysen

                                    contentDescription = "Add",

                                    modifier = Modifier.size(32.dp)

                                )

                            },

                            selected = false,

                            onClick = { /* Ekleme işlemi */ },

                            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White)

                        )



// 4. GRUP İKONU

                        NavigationBarItem(

                            icon = { Icon(painterResource(id = R.drawable.icon_groups), contentDescription = "Group") },

                            selected = false,

                            onClick = { /* Grup sayfası */ },

                            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White.copy(alpha = 0.6f))

                        )



// 5. PROFİL İKONU

                        NavigationBarItem(

                            icon = { Icon(painterResource(id = R.drawable.icon_profile), contentDescription = "Profile") },

                            selected = false,

                            onClick = { /* Profil sayfası */ },

                            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White.copy(alpha = 0.6f))

                        )

                    }

                }

            ) { innerPadding ->

// Sayfaların Değiştiği Alan (NavHost)

                NavHost(

                    navController = navController,

                    startDestination = "home", // Uygulama ilk açıldığında EV ekranı gelsin

                    modifier = Modifier.padding(innerPadding)

                ) {

// Home Ekranı

                    composable("home") {

                        HomePage()

                    }



// Ders Listesi Ekranı

                    composable("courses") {

                        CourseListScreen(

                            onCourseClick = { courseId ->

                                navController.navigate("course_detail/$courseId")

                            }

                        )

                    }



// Ders Detay Ekranı

                    composable(

                        route = "course_detail/{courseId}",

                        arguments = listOf(navArgument("courseId") { type = NavType.IntType })

                    ) { backStackEntry ->

                        val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

                        CourseDetailScreen(

                            courseId = courseId,

                            onBackClick = { navController.popBackStack() }

                        )

                    }

                }

            }

        }

    }

}