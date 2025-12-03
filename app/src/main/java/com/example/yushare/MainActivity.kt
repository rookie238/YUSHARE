package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yushare.navigation.HomeWithNavBar
import com.example.yushare.Screens.LoginScreen // LoginScreen'in paketine göre değişebilir
import com.example.yushare.ui.theme.YUSHARETheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val auth = FirebaseAuth.getInstance()

        // --- GEÇİCİ KOD BAŞLANGICI ---
        // Bu satır her uygulama açıldığında oturumu kapatır.
        // Böylece hep Login ekranı gelir. Testin bitince bu satırı sil.
        auth.signOut()
        // --- GEÇİCİ KOD BİTİŞİ ---

        val currentUser = auth.currentUser

        setContent {
            YUSHARETheme {
                AppNavigator(isLoggedIn = currentUser != null)
            }
        }
    }
}

@Composable
fun AppNavigator(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("home") { popUpTo("login") { inclusive = true } }
            })
        }
        composable("home") {
            HomeWithNavBar()
        }
    }
}