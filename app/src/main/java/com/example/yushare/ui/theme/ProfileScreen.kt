package com.example.yushare.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R // R harfi kırmızıysa burayı silip kendi paketini seç

@Composable
fun ProfileScreen() {
    // 1. ANA KUTU (Tüm ekranı kaplar)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // A. ARKA PLAN RESMİ (En altta)
        Image(
            painter = painterResource(id = R.drawable.profil_arkaplan),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // B. İÇERİK KATMANI (Resmin üstünde)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp) // Sağdan soldan boşluk
        ) {

            // Üstten biraz boşluk bırakalım (Status bar için)
            Spacer(modifier = Modifier.height(25.dp))

            // --- HEADER ALANI (Yazı ve Top) ---
            Box(
                modifier = Modifier.fillMaxWidth() // Enine tam kapla
            ) {
                // Yazı (Tam Ortada)
                Text(
                    text = "PROFILE",
                    color = Color(0xFF23006A), // Figma Moru
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.align(Alignment.Center) // Kendi kutusunun ortasında
                )

                // Gri Top (En Sağda)
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC4C4C4))
                        .align(Alignment.CenterEnd) // Kendi kutusunun sağında
                )
            }
            // --- HEADER BİTTİ ---
            // Üstteki Header ile bu kısım arasına biraz boşluk
            Spacer(modifier = Modifier.height(48.dp)) // Biraz daha aşağı itelim

            // --- BAŞLANGIÇ: SADECE AVATAR KISMI ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Ortadaki avatarları ortala
            ) {
                // 1. BÜYÜK GRİ YUVARLAK (Arka plan deseniyle uyumlu, Figma'dan tam renk/boyut al!)
                Box(
                    modifier = Modifier
                        .size(150.dp) // Figma'dan boyutu al (W/H)
                        .clip(CircleShape) // Yuvarlak kes
                        .background(Color(0xFFE0E0E0)), // Açık gri renk (Figma'dan tam kodu al)
                    contentAlignment = Alignment.Center // İçindeki beyazı ortala
                ) {
                    // 2. KÜÇÜK BEYAZ YUVARLAK (Bunun içinde avatar olacak)
                    Box(
                        modifier = Modifier
                            .size(100.dp) // Figma'dan boyutu al
                            .clip(CircleShape)
                            .background(Color.White), // Bembeyaz
                        contentAlignment = Alignment.Center // İçindeki avatarı ortala
                    ) {
                        // 3. AVATAR RESMİ
                        Image(
                            painter = painterResource(id = R.drawable.avatar_placeholder), // Senin avatar resmin
                            contentDescription = "Profil Avatarı",
                            contentScale = ContentScale.Crop, // Resmi orantılı sığdır
                            modifier = Modifier.size(80.dp) // Avatarın boyutu (Figma'dan al)
                        )
                    }
                }
            }
            // --- BİTİŞ: SADECE AVATAR KISMI ---

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}