package com.example.yushare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R

// --- 1. FONT VE RENKLER ---
val balooFontProfile = FontFamily(
    Font(R.font.baloo2_medium, FontWeight.Medium),
    Font(R.font.baloo2_medium, FontWeight.Bold),
    Font(R.font.baloo2_medium, FontWeight.ExtraBold),
    Font(R.font.baloo2_medium, FontWeight.SemiBold)
)

val ProfileBgColor = Color(0xFFEFEDE6)
val ProfileOrange = Color(0xFFFE9000)
val ProfileBlue = Color(0xFF294BA3)

// --- 2. ANA FONKSİYON (İsmi düzelttim: ProfileScreen) ---
@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBgColor)
    ) {
        // --- A) TURUNCU HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                .background(ProfileOrange)
        ) {
            // Üst Menü İkonları
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Geri Git */ },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ProfileBlue
                    )
                }

                Text(
                    text = "MY PROFILE",
                    color = ProfileBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = balooFontProfile
                )

                IconButton(
                    onClick = { /* Menü Aç */ },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = ProfileBlue
                    )
                }
            }
        }

        // --- B) ORTA İÇERİK (Avatar + İsim) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(110.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(283.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_avatar),
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // İsim
            Text(
                text = "Arda Demir",
                color = ProfileBlue,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = balooFontProfile
            )
        }

        // --- C) ALT MENÜ (Bottom Bar) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = ProfileBlue,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // İKON İSİMLERİNİ KONTROL ET
                Icon(painter = painterResource(id = R.drawable.ic_nav_home), contentDescription = "Home", tint = Color.White, modifier = Modifier.size(32.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_school), contentDescription = "School", tint = Color.White, modifier = Modifier.size(36.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_add), contentDescription = "Add", tint = Color.White, modifier = Modifier.size(44.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_groups), contentDescription = "Groups", tint = Color.White, modifier = Modifier.size(36.dp))
                Icon(painter = painterResource(id = R.drawable.ic_nav_profile), contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
    }
}

// --- 3. PREVIEW FONKSİYONU ---
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}