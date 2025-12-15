package com.example.yushare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.yushare.R

val balooFont = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.baloo2_medium, FontWeight.Medium)
)

@Composable
fun NotificationPreferencesDialog(
    onDismiss: () -> Unit // Kapatma veya Geri dönme işlemi
) {
    Dialog(onDismissRequest = onDismiss) {
        // --- ANA ÇERÇEVE (Edit Profile ile Aynı) ---
        Surface(
            modifier = Modifier
                .width(284.dp)
                .height(408.dp),
            shape = RoundedCornerShape(29.dp),
            color = Color(0xFF35414C).copy(alpha = 0.72f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // --- 1. ÜST BAŞLIK (Geri Ok + Yazı) ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Geri Butonu (Yuvarlak Gri)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp) // Göz kararı boyut
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)) // Hafif şeffaf beyaz arka plan
                            .clickable { onDismiss() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Başlık
                    Text(
                        text = "Notification Preferences",
                        style = TextStyle(
                            fontFamily = balooFont, // Baloo 2 fontu
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 2. LİSTE İÇERİĞİ ---
                // Çok madde olduğu için sığmazsa kaydırılabilsin diye Scroll ekledim
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    // KATEGORİ: TRANSFERS
                    CategoryTitle(text = "TRANSFERS")
                    NotificationRow(text = "Upload Completed", initialChecked = true)
                    NotificationRow(text = "Download Completed", initialChecked = false)
                    NotificationRow(text = "New File Received", initialChecked = true)

                    Spacer(modifier = Modifier.height(16.dp))

                    // KATEGORİ: GROUPS
                    CategoryTitle(text = "GROUPS")
                    NotificationRow(text = "Upload Completed", initialChecked = true)
                    NotificationRow(text = "Download Completed", initialChecked = true)
                    NotificationRow(text = "New File Received", initialChecked = false)

                    Spacer(modifier = Modifier.height(16.dp))

                    // KATEGORİ: GENERAL
                    CategoryTitle(text = "GENERAL")
                    NotificationRow(text = "App Updates", initialChecked = false)
                }
            }
        }
    }
}

// --- YARDIMCI BİLEŞENLER (Tekrar tekrar yazmamak için) ---

@Composable
fun CategoryTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = balooFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textDecoration = TextDecoration.Underline // Altı çizili
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun NotificationRow(text: String, initialChecked: Boolean) {
    var isChecked by remember { mutableStateOf(initialChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp), // Satırlar arası boşluk
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = balooFont,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        )

        // Switch (Aç/Kapa Anahtarı)
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier.scale(0.6f)
                .height(15.dp),
            colors = SwitchDefaults.colors(
                //(ON)
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF34C759),
                //(OFF)
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD9D9D9),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
// Modifier.scale kullanmak için import gerekebilir: androidx.compose.ui.draw.scale
fun Modifier.scale(scale: Float) = this.then(androidx.compose.ui.Modifier.graphicsLayer(scaleX = scale, scaleY = scale))
// Not: Yukarıdaki scale fonksiyonu yerine import androidx.compose.ui.draw.scale eklemen yeterli.

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun NotificationPreferencesPreview() {
    // Sadece tasarımın nasıl durduğunu görmek için boş bir kutu içinde çağırıyoruz
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        NotificationPreferencesDialog(
            onDismiss = {}
        )
    }
}