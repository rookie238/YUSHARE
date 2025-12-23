package com.example.yushare

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    // Uygulama içindeki sayfa geçişlerini yöneten ana kontrolcü
    val navController = rememberNavController()

    // NavHost: Sayfaların (destinations) içinde barındığı ve geçişlerin tanımlandığı ana yapı
    // startDestination = "course_list" -> Uygulama açıldığında ilk görünecek ekran
    NavHost(navController = navController, startDestination = "course_list") {

        // "course_list" rotası için tanımlanan sayfa (Ders Listesi Ekranı)
        composable("course_list") {
            CourseListScreen(
                // Bir kursa tıklandığında yapılacak işlem: detay sayfasına ID ile yönlendirir
                onCourseClick = { courseId ->
                    navController.navigate("course_detail/$courseId")
                },

                // Geri tuşuna basıldığında yapılacak işlem: bir önceki sayfaya döner
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // "course_detail/{courseId}" rotası (Ders Detay Ekranı)
        // Bu rota yanına süslü parantez içinde bir 'courseId' argümanı bekler
        composable(
            route = "course_detail/{courseId}",
            // Argümanın tipini (IntType) burada tanımlıyoruz
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->

            // Navigasyon sırasında gönderilen 'courseId' değerini çekiyoruz
            // Eğer değer bulunamazsa varsayılan olarak 0 atanır
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

            // Detay ekranını çağırıyoruz ve içindeki fonksiyonları tanımlıyoruz
            CourseDetailScreen(
                courseId = courseId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}