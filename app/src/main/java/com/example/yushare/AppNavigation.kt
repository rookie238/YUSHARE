package com.example.yushare

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "course_list") {

        // 1. Ekran: Liste Ekranı
        composable("course_list") {
            CourseListScreen(
                onCourseClick = { courseId ->
                    // Burası önemli: ID (Int) navigasyona String olarak eklenir
                    navController.navigate("course_detail/$courseId")
                }
            )
        }

        // 2. Ekran: Detay Ekranı
        composable(
            route = "course_detail/{courseId}",
            // AŞAĞIDAKİ KISIM HATAYI ÇÖZEN KISIMDIR
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->

            // Veriyi güvenli bir şekilde Int (Sayı) olarak alıyoruz
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

            CourseDetailScreen(
                courseId = courseId, // Artık buraya String değil, Int gidiyor
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}