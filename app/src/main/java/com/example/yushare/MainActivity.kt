package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue // BU ÇOK ÖNEMLİ
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // BU ÇOK ÖNEMLİ
import com.example.yushare.ui.screens.MenuScreen
import com.example.yushare.ui.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Hangi ekranda olduğumuzu tutan değişken
            // Başlangıçta "profile" ekranı açık olsun
            var currentScreen by remember { mutableStateOf("profile") }

            if (currentScreen == "profile") {
                // --- PROFİL EKRANI ---
                ProfileScreen(
                    onMenuClick = {
                        // Butona basılınca burası çalışır ve ekranı değiştirir
                        currentScreen = "menu"
                    }
                )
            } else {
                // --- MENÜ EKRANI ---
                MenuScreen(
                    onCloseClick = {
                        // Çarpıya basılınca tekrar Profile döner
                        currentScreen = "profile"
                    }
                )
            }
        }
    }
}