package com.example.yushare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R

// --- 1. FONT VE RENKLER ---
val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)

val ProfileBgColor = Color(0xFFEFEDE6)
val ProfileOrange = Color(0xFFFE9000)
val ProfileBlue = Color(0xFF294BA3)
val ProfileInfoBg = Color(0xFFD9D9D9)

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
                    .padding(top = 27.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Geri Butonu
                IconButton(
                    onClick = { /* Geri Git */ },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = Color(0xFFD7F171),
                            shape = CircleShape)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "MY PROFILE",
                    color = ProfileBlue,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily
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
            Spacer(modifier = Modifier.height(95.dp))

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
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.height(20.dp))

            // BİLGİ KARTI
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp) // Sağdan soldan biraz boşluk
                    .background(
                        color = ProfileInfoBg,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(vertical = 50.dp, horizontal = 24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // -- Satır 1: Department --
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Department: ")
                            }
                            append("Visual Communication Design")
                        },
                        color = ProfileBlue,
                        fontSize = 15.sp,
                        fontFamily = poppinsFontFamily
                    )
                    // -- Satır 2: Student Id --
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Student Id: ")
                            }
                            append("20210584978")
                        },
                        color = ProfileBlue,
                        fontSize = 15.sp,
                        fontFamily = poppinsFontFamily
                    )
                    // -- Satır 3: Bio --
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Bio: ")
                            }
                            append(" Designing the world, one pixel at a time. Visual Communication Design Student. Powered by coffee and creativity. Always open to new projects and collaborations. ")
                        },
                        color = ProfileBlue,
                        fontSize = 14.sp, // Bio biraz daha küçük olabilir
                        fontFamily = poppinsFontFamily,
                        lineHeight = 20.sp
                    )
                }
            }
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