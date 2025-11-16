package com.example.yushare.ui.theme
// ProfileScreen.kt dosyanızın içi

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.yushare.ui.theme.AppPurple // Renk dosyanı import etmeyi unutma!

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar için bu gerekli
@Composable
fun ProfileTopBar() {
    TopAppBar(
        // Başlık (Title)
        title = {
            Text(
                text = "PROFILE",
                color = AppPurple, // Figma'dan aldığımız mor renk
            )
        },

        // Sağdaki Hamburger Menü İkonu
        actions = {
            IconButton(onClick = { /* Tıklanınca ne olacağını buraya yazacağız */ }) {
                Icon(
                    imageVector = Icons.Default.Menu, // Hazır Material ikonu
                    contentDescription = "Menü",
                    tint = Color.Gray // Tasarımdaki griye benziyor, TextGray de olabilir
                )
            }
        },

        // Arka plan rengi. Tasarımda beyaz görünüyor.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

// Bu fonksiyon, yazdığımız kodun önizlemesini Android Studio'da görmemizi sağlar.
@Preview(showBackground = true)
@Composable
fun ProfileTopBarPreview() {
    ProfileTopBar()
}
