package com.example.yushare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
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


@Composable
fun ProfileScreen(onMenuClick: () -> Unit) {
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
                            shape = CircleShape
                        )

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
                    onClick =  onMenuClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = Color(0xFFD7F171),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_burger),
                        contentDescription = "Menu",
                        tint = Color.Black
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
            Spacer(modifier = Modifier.height(1.dp))
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

            Spacer(modifier = Modifier.height(10.dp))

            // İsim
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Arda Demir",
                    color = ProfileBlue,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFontFamily,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // BİLGİ KARTI
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp) // Sağdan soldan biraz boşluk
                    .background(
                        color = Color(0xFFD9D9D9),
                        shape = RoundedCornerShape( 50.dp)
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
        var selectedItemIndex by remember { mutableStateOf(4) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp) // Yüksekliği
                .background(ProfileBlue) // Direkt mavi renk (Çizim yok)
                .align(Alignment.BottomCenter) // Ekranın en altına yapıştır
        ) {
            // İkonları Yan Yana Diz
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround, // Eşit aralıklarla diz
                verticalAlignment = Alignment.CenterVertically // Dikeyde ortala
            ) {
                // 0: Ana Sayfa
                IconButton(onClick = { selectedItemIndex = 0 }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_home),
                        contentDescription = "Home",
                        tint = if (selectedItemIndex == 0) Color.White else Color.White.copy(alpha = 0.6f), // Seçili değilse biraz soluk yapalım mı?
                        modifier = Modifier.size(30.dp)
                    )
                }

                // 1: Okul
                IconButton(onClick = { selectedItemIndex = 1 }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_school),
                        contentDescription = "School",
                        tint = if (selectedItemIndex == 1) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(34.dp)
                    )
                }

                // 2: Ekle (+)
                IconButton(onClick = { selectedItemIndex = 2 }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_add),
                        contentDescription = "Add",
                        tint = if (selectedItemIndex == 2) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(40.dp)
                    )
                }

                // 3: Gruplar
                IconButton(onClick = { selectedItemIndex = 3 }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_groups),
                        contentDescription = "Groups",
                        tint = if (selectedItemIndex == 3) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(34.dp)
                    )
                }

                // 4: Profil
                IconButton(onClick = { selectedItemIndex = 4 }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_profile),
                        contentDescription = "Profile",
                        tint = if (selectedItemIndex == 4) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

// --- 3. PREVIEW FONKSİYONU ---
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onMenuClick = {})
}
