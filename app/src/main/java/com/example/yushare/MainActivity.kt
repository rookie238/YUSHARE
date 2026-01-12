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
import com.example.yushare.screens.OnboardingScreen // <-- BU IMPORT'U EKLEMELİSİN
import com.example.yushare.screens.RegisterScreen
import com.example.yushare.ui.theme.YUSHARETheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            YUSHARETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // DEĞİŞİKLİK 1: startDestination "onboarding" yapıldı
                    NavHost(navController = navController, startDestination = "onboarding") {

                        // DEĞİŞİKLİK 2: Onboarding rotası eklendi
                        composable("onboarding") {
                            OnboardingScreen(navController = navController)
                        }

                        // 1. LOGIN
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        // Geri tuşuna basınca login'e dönmesin diye stack temizleniyor
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
                        composable("home") {
                            HomeWithNavBar()
                        }
                    }
                }
            }
        }
    }
}