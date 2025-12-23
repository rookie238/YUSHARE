package com.example.yushare.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Koyu Tema Renkleri
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Açık Tema Renkleri (Hata buradaydı, düzeltildi)
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    // Eğer projenizdeki bej rengini (YuBackground) tüm uygulamanın
    // arka planı yapmak isterseniz yorum satırını kaldırıp şunu ekleyin:

    // background = YuBackground,
    // surface = YuBackground,


    /* Diğer varsayılan renkleri değiştirmek isterseniz:
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun YUSHARETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color Android 12+ cihazlarda duvar kağıdına göre renk alır.
    // Kendi renklerinizi zorlamak istiyorsanız bunu 'false' yapın.
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}