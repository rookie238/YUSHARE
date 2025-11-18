package com.example.yushare.ui.screens

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
                // Avatar ile Yazılar arasına boşluk
                Spacer(modifier = Modifier.height(16.dp))

                // --- BİLGİLER KISMI (SOLA YASLI) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 1.dp) // Soldan hizalama boşluğu (Göz kararı)
                ) {
                    // 1. İSİM
                    Text(
                        text = "Arda Demir",
                        color = Color(0xFF23006A), // Koyu Mor
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )

                    Spacer(modifier = Modifier.height(30.dp)) // İsim ile bölüm arası boşluk

                    // 2. BÖLÜM (Department)
                    // Tek satırda iki farklı renk yapmak için basit yol: Row
                    Row {
                        Text(
                            text = "Department: ",
                            color = Color(0xFF23006A), // Mor Başlık
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Visual Communication Design",
                            color = Color(0xFF5F5C6B), // Gri Yazı
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp)) // Satır arası boşluk

                    // 3. ÖĞRENCİ NO (Student Id)
                    Row {
                        Text(
                            text = "Student Id: ",
                            color = Color(0xFF23006A), // Mor Başlık
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "20210584978",
                            color = Color(0xFF5F5C6B), // Gri Yazı
                            fontSize = 14.sp
                        )
                    }
                }
                // --- BİLGİLER BİTTİ ---
                // 1. Aşağıya doğru boşluk bırak (O alttaki gri daireye denk gelsin)
                Spacer(modifier = Modifier.height(150.dp))

                // 2. NOTES ALANI
                Column(
                    modifier = Modifier
                        .padding(start = 1.dp) // Yukarıdaki yazılarla aynı hizada başlasın
                ) {
                    // Başlık
                    Text(
                        text = "Notes:",
                        color = Color(0xFF23006A), // Koyu Mor
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.SansSerif
                    )

                    // BURASI ÖNEMLİ:
                    // Tasarımdaki o "Mavi Kutu" alanı kadar boşluk bırakıyoruz.
                    // Böylece alt menü geldiğinde yazının üstüne binmeyecek.
                    Box(
                        modifier = Modifier
                            .width(300.dp)  // Figma'daki genişlik
                            .height(150.dp) // Figma'daki yükseklik
                        // .border(1.dp, Color.Red) // Kodu test ederken kutuyu görmek istersen bunu aç
                    ) {
                        // İstersen buraya silik bir yazı koyabilirsin
                        Text(
                            text = "",
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
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