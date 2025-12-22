package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yushare.navigation.HomeWithNavBar
import com.example.yushare.screens.ForgotPasswordScreen
import com.example.yushare.screens.LoginScreen
import com.example.yushare.screens.PostDetailScreen // BU IMPORT ÖNEMLİ (Eğer kırmızı yanarsa aşağıyı oku)
import com.example.yushare.screens.RegisterScreen
import com.example.yushare.ui.theme.YUSHARETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YUSHARETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ana navigasyon kontrolcüsü
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {

                        // 1. LOGIN
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. REGISTER
                        composable("register") {
                            RegisterScreen(navController = navController)
                        }

                        // 3. FORGOT PASSWORD
                        composable("forgot_password") {
                            ForgotPasswordScreen(navController = navController)
                        }

                        // 4. HOME (Bottom Navigation Bar'ı olan ana ekran)
                        composable("home") {
                            // ÖNEMLİ NOT: HomeWithNavBar içindeki HomeScreen'den buradaki detay sayfalarına
                            // gidebilmek için, HomeWithNavBar'ın root navController'a erişimi olması gerekebilir.
                            // Eğer HomeWithNavBar kendi içinde ayrı bir navController oluşturuyorsa,
                            // root navController'ı ona parametre olarak geçmen gerekebilir.
                            HomeWithNavBar()
                        }

                        // --- 5. EKLENEN KISIM: POST DETAY SAYFASI ---
                        // Yorum veya gönderiye tıklayınca buraya gelecek
                        composable("post_detail_placeholder") {
                            PostDetailScreen(navController = navController)
                        }

                        // --- 6. OPSİYONEL: KURS DETAY SAYFASI ---
                        // İleride kurslara tıklayınca çökmemesi için bunu da ekleyebilirsin
                        // composable("course_detail/{courseId}") { ... }
                    }
                }
            }
        }
    }
}