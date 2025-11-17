package com.example.yushare.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.yushare.R

// Uygulamada kullanacağımız Lalezar font ailesi
val LalezarFont = FontFamily(
    Font(
        resId = R.font.lalezar_regular,
        weight = FontWeight.Normal
    )
)

// Material 3 Typography’yi Lalezar ile özelleştirilmiş hâli
val Typography = Typography(
    // Başlıklar
    titleLarge = TextStyle(
        fontFamily = LalezarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = LalezarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),

    // Gövde yazıları
    bodyLarge = TextStyle(
        fontFamily = LalezarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = LalezarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // Diğer stiller default kalsın (Typography() içindeki varsayılanlar)
)
