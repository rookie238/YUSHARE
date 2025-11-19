package com.example.yushare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close // Kapatma (X) ikonu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit // Pencereyi kapatma emri
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp), // Figma'ya yakın yuvarlaklık
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6C7B8E)), // Figma'daki gri ton
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f) // Genişliğini ayarla, Figma'ya göre
                .padding(vertical = 40.dp) // Dikeyde biraz boşluk
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // BAŞLIK VE KAPAT BUTONU
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SETTINGS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // MENÜ LİSTESİ
                val menuItems = listOf(
                    "Edit Profile",
                    "Notification Prefrences",
                    "My Feedback Archive",
                    "Privacy and Visibility",
                    "Data Privacy Policy",
                    "Help & Support",
                    "Log Out"
                )

                menuItems.forEach { item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Tıklanınca ilgili sayfaya gitme */ }
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        )
                        // Ayırıcı çizgi (Log Out hariç)
                        if (item != "Log Out") {
                            Divider(
                                color = Color.White.copy(alpha = 0.4f), // Hafif şeffaf beyaz çizgi
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Preview için (Hatanın düzeldiğini görmek için)
@Preview(showBackground = true)
@Composable
fun SettingsDialogPreview() {
    SettingsDialog(onDismiss = {})
}