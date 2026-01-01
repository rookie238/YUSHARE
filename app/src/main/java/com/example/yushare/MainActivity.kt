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
                    // Ana navigasyon kontrolcüsü (Login -> Home arası geçiş için)
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

                        // 4. HOME
                        // Burası açıldığında "HomeWithNavBar" devreye girer.
                        // Post detayları ve diğer alt sayfalar "AppNavigation" (HomeWithNavBar'ın içi) tarafından yönetilir.
                        // O yüzden buraya ekstradan PostDetailScreen yazmana gerek yoktur.
                        composable("home") {
                            HomeWithNavBar()
                        }
                    }
                }
            }
        }
    }
}