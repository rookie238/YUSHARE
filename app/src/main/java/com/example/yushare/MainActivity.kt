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
import com.example.yushare.screens.LoginScreen
import com.example.yushare.ui.theme.YUSHARETheme
import com.google.firebase.auth.FirebaseAuth
// --- 1. IMPORT (Cloudinary) ---
import com.cloudinary.android.MediaManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- 2. CLOUDINARY BAŞLATMA (BİLGİLERİN GİRİLDİ) ---
        try {
            val config = HashMap<String, String>()

            // Senin Cloud Name'in:
            config["cloud_name"] = "dqftx42nh"

            // Senin API Key'in:
            config["api_key"] = "228968633971553"

            MediaManager.init(this, config)
        } catch (e: Exception) {
            // Uygulama tekrar açıldığında çökmemesi için boş geçiyoruz.
        }
        // -------------------------------------------------

        enableEdgeToEdge()
        val auth = FirebaseAuth.getInstance()

        // --- GEÇİCİ KOD (HER AÇILIŞTA GİRİŞ EKRANI GELİR) ---
        // Uygulamayı tamamladığında bu satırı silmeyi unutma!
        //auth.signOut()
        // -----------------------------------------------------

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