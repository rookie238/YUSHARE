package com.example.yushare.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.yushare.R

// Baloo Fontunu burada da tanımlayalım (veya ortak bir yerden çekebilirsin)
val balooFontSettings = FontFamily(
    Font(R.font.baloo2_medium, FontWeight.Medium),
    Font(R.font.baloo2_medium, FontWeight.Bold),
    Font(R.font.baloo2_medium, FontWeight.ExtraBold)
)

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current
    var showNotificationPrefs by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        // --- ANA ÇERÇEVE (Diğerleriyle Aynı Stil) ---
        Surface(
            modifier = Modifier
                .width(300.dp) // Genişlik ayarı
                .heightIn(min = 300.dp, max = 500.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF35414C).copy(alpha = 0.72f) // Koyu, şeffaf tema rengimiz
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- 1. BAŞLIK VE KAPAT BUTONU ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                    ) {
                        Text(
                            text = "SETTINGS",
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(
                                fontFamily = balooFontSettings,
                                fontSize = 16.sp, // Figma'dan aldığımız boyut
                                fontWeight = FontWeight.SemiBold, // Figma'dan aldığımız kalınlık
                                color = Color.White,
                                letterSpacing = 0.sp
                            )
                        )

                        //2. x butonu
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .size(32.dp)
                                .clickable { onDismiss() }
                        ) {
                            Text(
                                text = "X",
                                style = TextStyle(
                                    fontFamily = balooFontSettings, // Baloo 2
                                    fontSize = 17.sp, // Figma: 17
                                    fontWeight = FontWeight.SemiBold, // Figma: SemiBold
                                    color = Color.White // Figma: #FFFFFF
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- 2. MENÜ LİSTESİ ---
                    val menuItems = listOf(
                        "Edit Profile",
                        "Notification Preferences",
                        "My Feedback Archive",
                        "Privacy and Visibility",
                        "Data Privacy Policy",
                        "Help & Support",
                        "Log Out"
                    )

                    menuItems.forEach { item ->
                        SettingsItemRow(
                            text = item,
                            onClick = {
                                when (item) {
                                    "Edit Profile" -> onEditProfileClick()
                                    "Notification Preferences" -> showNotificationPrefs = true
                                    else -> Toast.makeText(context, "$item seçildi", Toast.LENGTH_SHORT).show()
                                }
                            },
                            showDivider = item != "Log Out", // Log Out hariç çizgi çek
                            isLogOut = item == "Log Out" // Log Out rengi farklı olabilir
                        )
                    }
                }

                // --- 3. Notification Penceresi Çağırma ---
                if (showNotificationPrefs) {
                    NotificationPreferencesDialog(
                        onDismiss = { showNotificationPrefs = false }
                    )
                }
            }
        }
    }
}

// --- YARDIMCI BİLEŞEN: SATIR TASARIMI ---
@Composable
fun SettingsItemRow(
    text: String,
    onClick: () -> Unit,
    showDivider: Boolean,
    isLogOut: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Satır yüksekliği
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = balooFontSettings,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )

        }

        // Alt Çizgi
        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.15f) // Çok ince ve silik çizgi
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsDialogPreview() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Gray)) {
        SettingsDialog(onDismiss = {}, onEditProfileClick = {})
    }
}