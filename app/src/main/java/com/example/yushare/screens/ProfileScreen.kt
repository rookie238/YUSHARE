package com.example.yushare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.zIndex
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
val CardGray = Color(0xFFE0E0E0)

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

    // HomeWithNavBar zaten bir Scaffold sağladığı için burada sadece Column kullanıyoruz.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBgColor)
            .verticalScroll(rememberScrollState()), // Kaydırma özelliği
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- HEADER VE AVATAR ALANI ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            // 1. Turuncu Arka Plan (Header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                    .background(ProfileOrange)
            ) {
                // Üst Menü İkonları (Geri ve Menü)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Geri Butonu
                    CircleIconButton(
                        iconResId = R.drawable.ic_back_arrow,
                        onClick = {
                            if (!navController.popBackStack()) {
                                navController.navigate("home")
                            }
                        }
                    )

                    Text(
                        text = "MY PROFILE",
                        color = ProfileBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )

                    // Menü Butonu (Edit Profile veya Settings buraya bağlanabilir)
                    CircleIconButton(
                        iconResId = R.drawable.ic_menu_burger,
                        onClick = {
                            navController.navigate("menu")
                        }
                    )
                }
            }

            // 2. Avatar
            Box(
                modifier = Modifier
                    .padding(top = 150.dp)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .border(4.dp, Color.White, CircleShape)
                    .zIndex(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_avatar),
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // --- KULLANICI BİLGİLERİ ---
        Spacer(modifier = Modifier.height(16.dp))

        // DİNAMİK İSİM (Veritabanından)
        Text(
            text = userProfile.name,
            color = ProfileBlue,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFontFamily,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- BİLGİ KARTI ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(CardGray)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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

            // DİNAMİK DERECE (Eğer modelde degree varsa açabilirsin)
            if (userProfile.degree.isNotEmpty()) {
                ProfileInfoRow(label = "Degree:", value = userProfile.degree)
            }

            // --- BİO KISMI (GÜNCELLENDİ) ---
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

                // BURASI ARTIK VERİTABANINDAN GELİYOR
                Text(
                    text = userProfile.bio.ifEmpty { "Henüz bir biyografi eklenmemiş." },
                    color = ProfileBlue,
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Alttaki BottomBar payı
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

@Composable
fun CircleIconButton(iconResId: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .background(color = Color(0xFFD9D9D9), shape = CircleShape)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
    }
}