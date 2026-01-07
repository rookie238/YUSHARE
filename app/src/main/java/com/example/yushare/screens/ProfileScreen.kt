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
import androidx.compose.ui.tooling.preview.Preview
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

val ProfileBgColor = Color(0xFFEFEDE6) // Krem Rengi
val ProfileOrange = Color(0xFFFE9000)
val ProfileBlue = Color(0xFF294BA3)

// 1. ANA EKRAN (ViewModel burada bağlanır)
@Composable
fun ProfileScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    val userProfile = viewModel.currentUserProfile.value

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    // UI kısmını verilerle çağırıyoruz
    ProfileScreenContent(
        name = userProfile.name,
        department = userProfile.department,
        studentId = userProfile.studentId,
        degree = userProfile.degree,
        bio = userProfile.bio,
        onBackClick = { navController.popBackStack() },
        onMenuClick = { navController.navigate("menu") }
    )
}

// 2. SADECE GÖRÜNÜM (Tasarım burası - Preview için ayrıldı)
@Composable
fun ProfileScreenContent(
    name: String,
    department: String,
    studentId: String,
    degree: String,
    bio: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBgColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp))
                    .background(ProfileOrange)
            ) {
                // ÜST MENÜ (Geri ve Menü butonları)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFFD9D9D9), CircleShape)
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )

                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFFD9D9D9), CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu_burger),
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                }
            }

            // 2. AVATAR VE İSİM (ÖNEMLİ DEĞİŞİKLİK BURADA)
            // İsim artık bu Column'un içinde, yani turuncu alanın üstünde duracak.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Üstten boşluk (Avatarı aşağı itmek için)
                Spacer(modifier = Modifier.height(110.dp))

                // AVATAR
                Box(
                    modifier = Modifier
                        .size(270.dp)
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


                Text(
                    text = name,
                    color = ProfileBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(Color(0xFFE6E8EB), RoundedCornerShape(40.dp)) // Rengi biraz daha açtım (Figma gibi)
                .padding(vertical = 40.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileInfoRow(label = "Department:", value = department)
            ProfileInfoRow(label = "Student Id:", value = studentId)

            if (degree.isNotEmpty()) {
                ProfileInfoRow(label = "Degree:", value = degree)
            }

            // BİO
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
                    text = bio.ifEmpty { "Henüz bir biyografi eklenmemiş." },
                    color = ProfileBlue,
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

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
