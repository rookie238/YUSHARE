package com.example.yushare.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.yushare.R

// Rengi buraya sabitledim, hata vermesin diye
val MenuBlue = Color(0xFF294BA3)

@Composable
fun MenuScreen(onCloseClick: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
    ) {
        // --- İÇERİK ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- TURUNCU ALAN ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(
                        color = Color(0xFFFF9800),
                        shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(1.dp))
                    // Üst Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, end = 20.dp)
                    ) {
                        Text(
                            text = "My Profile",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MenuBlue,
                            fontFamily = poppinsFontFamily,
                            modifier = Modifier.align(Alignment.Center)
                        )


                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(283.dp)
                            .clip(RoundedCornerShape(283.dp))
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Arda Demir",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily,
                        color = MenuBlue
                    )

                    Text(
                        text = "arda.demir@std.yeditepe.edu.tr",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppinsFontFamily,
                        color = MenuBlue.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onCloseClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .width(126.dp)
                            .height(43.dp)
                    ) {
                        Text(
                            "Edit Profile",
                            color = MenuBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFontFamily
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            // Liste Elemanları (Aralarına 10dp boşluk koyuyoruz - Figma'daki sıklığa göre)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp), // Elemanlar arası boşluk
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem(text = "Change Password")
                MenuItem(text = "Notification Preferences")
                MenuItem(text = "My Feedback Archive")
                MenuItem(text = "Privacy and Visibility")
                MenuItem(text = "Data Privacy Policy")
                MenuItem(text = "Help & Support")

                Box(
                    modifier = Modifier
                        .width(366.dp)
                        .height(38.dp)
                        .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp))
                        .clickable {
                            (context as? android.app.Activity)?.finish()
                        }
                        .padding(horizontal = 10.dp), // Soldan boşluk
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Logout",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold, // Figma: SemiBold
                            fontSize = 16.sp,
                            color = Color(0xFFBF0000) // Figma: Kırmızı #BF0000
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Yazı ile ok arası boşluk
                        Icon(
                            painter = painterResource(id = R.drawable.vector), // BURAYA KENDİ DOSYA ADINI YAZ
                            contentDescription = "Logout Arrow",
                            tint = Color(0xFFBF0000), // İkonu da kırmızı yapıyoruz
                            modifier = Modifier.size(16.dp) // İkon boyutu (Figma'ya göre ayarla)
                        )
                    }
                }
            }
            }

        // --- ALT BAR (Bottom Bar) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(MenuBlue)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(painter = painterResource(id = R.drawable.ic_nav_home), contentDescription = null, tint = Color.White.copy(0.6f), modifier = Modifier.size(30.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_school), contentDescription = null, tint = Color.White.copy(0.6f), modifier = Modifier.size(34.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_add), contentDescription = null, tint = Color.White.copy(0.6f), modifier = Modifier.size(40.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_groups), contentDescription = null, tint = Color.White.copy(0.6f), modifier = Modifier.size(34.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_profile), contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@Composable
fun MenuOptionItem(text: String, isLogout: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 6.dp)
            .height(55.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = if (isLogout) Color(0xFFC00000) else MenuBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
// --- YARDIMCI FONKSİYON: Menü Satırı ---
@Composable
fun MenuItem(text: String, textColor: Color = Color(0xFF294BA3)) { // Varsayılan renk Mavi
    Box(
        modifier = Modifier
            .width(366.dp) // Figma: W 366
            .height(38.dp) // Figma: H 38
            .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp)) // Figma: D9D9D9, Radius 10
            .clickable { }
            .padding(horizontal = 10.dp), // Figma: X farkı 10px
        contentAlignment = Alignment.CenterStart // Yazıyı dikeyde ortala, solda tut
    ) {
        Text(
            text = text,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium, // Figma: Medium
            fontSize = 16.sp, // Figma: 16
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen(onCloseClick = {})
}