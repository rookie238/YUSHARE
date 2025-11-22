package com.example.yushare.ui.screens
import androidx.compose.foundation.Image



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// R dosyasını import etmeyi unutma (com.example.projeadi.R)

@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    currentBio: String
) {
    var bioText by remember { mutableStateOf(currentBio) }

    // --- 🎨 FİGMA RENK PALETİ (REVİZE EDİLDİ) ---
    // Arka plan: Daha mat, koyu gri (Slate Grey)
    val dialogBackgroundColor = Color(0xFF35414C).copy(alpha = 0.72f)
    val dialogCornerRadius = 29.dp

    // Bio Kutusu: Daha açık, gümüş gri
    val bioBoxColor = Color(0xFFD9D9D9)

    // Bio Yazısı: Koyu antrasit
    val bioTextColor = Color(0xFF2D3436)

    // Artı (+) İkonu: Figma'daki gibi koyu mor/indigo
    val plusIconColor = Color(0xFF4527A0)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .width(284.dp)
                .height(408.dp)
                .clip(RoundedCornerShape(dialogCornerRadius))// Figma'daki yumuşak köşe
                .background(dialogBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp), // İçeriden boşluk
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 1. Close (X) Butonu
                // Row kullanarak sağa yasladık, Box'a göre daha kontrollü
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp) // İkon boyutu küçültüldü (Daha zarif)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Profil Resmi (Avatar)
                Image(
                    // Buraya kendi resim kaynağını koy: painterResource(id = R.drawable.my_avatar)
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp) // Boyut Figma'ya göre biraz küçültüldü (daha orantılı)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Yazı ve Artı İkonu
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { /* Resim Değiş */ }
                ) {
                    Text(
                        text = "Change Profile Picture",
                        style = TextStyle(
                            fontSize = 12.sp, // Font boyutu küçüldü (Figma ile eşleşti)
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = plusIconColor, // Koyu Mor
                        modifier = Modifier.size(20.dp) // İkon küçüldü
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Bio Bölümü
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Bio",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )

                    // Bio Kutusu (Tamamen Custom)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp) // Yükseklik ayarlandı
                            .clip(RoundedCornerShape(16.dp))
                            .background(bioBoxColor) // Açık Gri Arkaplan
                            .padding(16.dp) // Yazının içeriden boşluğu
                    ) {
                        BasicTextField(
                            value = bioText,
                            onValueChange = { bioText = it },
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold, // "FEIN FEIN" kalınlığı
                                color = bioTextColor
                            ),
                            cursorBrush = SolidColor(plusIconColor),
                            modifier = Modifier.fillMaxSize()
                        )

                        // Eğer yazı boşsa "Placeholder" göstermek istersen:
                        if (bioText.isEmpty()) {
                            Text("Enter your bio...", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }

                // Alt tarafta biraz boşluk bırakalım ki kutu dibe yapışmasın
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// --- Preview Kısmı ---
@Preview(showBackground = true)
@Composable
fun EditProfilePixelPerfectPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF263238).copy(alpha = 0.8f)), // Arkadaki koyu flu his
        contentAlignment = Alignment.Center
    ) {
        EditProfileDialog(
            onDismiss = {},
            currentBio = "FEIN FEIN FEIN"
        )
    }
}