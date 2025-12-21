package com.example.yushare.ui.screens // ⚠️ BURAYA DİKKAT: Senin dosyanın en üstünde ne yazıyorsa onu yaz!

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import com.example.yushare.R

// Rengi buraya sabitledim, hata vermesin diye
val MenuBlue = Color(0xFF294BA3)

@Composable
fun MenuScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
    ) {
        // --- İÇERİK ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- TURUNCU ALAN ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .background(
                        color = Color(0xFFFF9800),
                        shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            modifier = Modifier.align(Alignment.Center)
                        )

                        IconButton(
                            onClick = { /* Geri Git */ },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(30.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ⚠️ DİKKAT: Buradaki resim ismini kendi projenle aynı yap (örn: R.drawable.profile_pic)
                    // Hata verirse burayı geçici olarak yorum satırı yap
                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(100))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Arda Demir",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MenuBlue
                    )

                    Text(
                        text = "arda.demir@std.yeditepe.edu.tr",
                        fontSize = 14.sp,
                        color = MenuBlue.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Edit Profile", color = MenuBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- LİSTE ---
            Spacer(modifier = Modifier.height(10.dp))
            MenuOptionItem(text = "Change Password", onClick = { })
            MenuOptionItem(text = "Notification Preferences", onClick = { })
            MenuOptionItem(text = "My Feedback Archive", onClick = { })
            MenuOptionItem(text = "Privacy and Visibility", onClick = { })
            MenuOptionItem(text = "Data Privacy Policy", onClick = { })
            MenuOptionItem(text = "Help & Support", onClick = { })

            Spacer(modifier = Modifier.height(10.dp))
            MenuOptionItem(text = "Logout", isLogout = true, onClick = { })
            Spacer(modifier = Modifier.height(40.dp))
        }

        // --- ALT BAR (Bottom Bar) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(MenuBlue)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ⚠️ DİKKAT: İkon isimlerini (R.drawable.xxx) kendi projendekilerle kontrol et
                // Hata verirse bu Icon satırlarını silip yerlerine Text("X") koyarak dene.
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
            if (isLogout) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Logout", tint = Color(0xFFC00000))
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}