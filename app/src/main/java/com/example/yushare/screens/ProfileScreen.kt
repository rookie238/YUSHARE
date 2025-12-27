package com.example.yushare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yushare.R
import com.example.yushare.viewmodel.SharedViewModel

// --- FONT VE RENKLER ---
val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)

val ProfileBgColor = Color(0xFFEFEDE6)
val ProfileOrange = Color(0xFFFE9000)
val ProfileBlue = Color(0xFF294BA3)

@Composable
fun ProfileScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    // 1. Veritabanından gelen kullanıcı verisini alıyoruz
    val userProfile = viewModel.currentUserProfile.value

    // 2. Sayfa açıldığında veriyi tazelemek için fetch fonksiyonunu çağırıyoruz
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    // Ana Kaydırılabilir Kolon
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBgColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {

            // 1. Turuncu Arka Plan (Header)
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
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Color(0xFFD9D9D9),
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

                    // Menü Butonu (DÜZELTİLDİ: Doğrudan 'menu' sayfasına yönlendiriyor)
                    IconButton(
                        onClick = { navController.navigate("menu") },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Color(0xFFD9D9D9),
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

            // Avatar
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
            }
        } // Header Box Sonu

        // --- KULLANICI İSMİ ---
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userProfile.name,
            color = ProfileBlue,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- BİLGİ KARTI ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    color = Color(0xFFD9D9D9),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(vertical = 50.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // DİNAMİK BÖLÜM
            ProfileInfoRow(
                label = "Department:",
                value = userProfile.department
            )

            // DİNAMİK ÖĞRENCİ NO
            ProfileInfoRow(
                label = "Student Id:",
                value = userProfile.studentId
            )

            // DİNAMİK DERECE (Varsa göster)
            if (userProfile.degree.isNotEmpty()) {
                ProfileInfoRow(label = "Degree:", value = userProfile.degree)
            }

            // --- BİO ---
            Column {
                Text(
                    text = "Bio:",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = ProfileBlue,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.bio.ifEmpty { "Henüz bir biyografi eklenmemiş." },
                    color = ProfileBlue,
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Sayfanın altındaki boşluk
    }
}

// --- YARDIMCI COMPONENTLER ---

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label ")
            }
            append(value)
        },
        color = ProfileBlue,
        fontSize = 15.sp,
        fontFamily = poppinsFontFamily
    )
}