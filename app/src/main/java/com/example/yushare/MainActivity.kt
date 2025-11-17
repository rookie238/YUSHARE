package com.example.yushare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.yushare.ui.theme.YUSHARETheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Bu kalabilir, sorun değil
        setContent {
            YUSHARETheme { // Senin temanı kullanıyoruz

                // ESKİ Scaffold'u sildik, yerine bunu koyduk:
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White // <-- 1. Düzeltme: Arka planı beyaz yaptık
                ) {

                    // ESKİ Greeting'i sildik, yerine bunu koyduk:
                    // 2. Düzeltme: ProfileScreen'i çağırıyoruz


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YUSHARETheme {
        Greeting("Android")
    }
}