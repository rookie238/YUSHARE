package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yushare.R
import com.example.yushare.model.Course
import com.example.yushare.model.UserProfile
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun CoursesScreen(
    viewModel: SharedViewModel,
    navController: NavHostController
) {
    val courses = viewModel.coursesList
    val userProfile = viewModel.currentUserProfile.value

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    val groupedCourses = courses.groupBy { it.term }.toSortedMap()

    // arkaplan rengi
    val BackgroundColor = Color(0xFFF2F1EC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // --- HEADER (Geri Butonu) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.2f), CircleShape) // Buton arkasına hafif fon
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF2B0B5E))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 100.dp), // Altta navigasyon bar için boşluk
            // ders kartlarını üst üste bindiren kısım
            verticalArrangement = Arrangement.spacedBy((-30).dp)
        ) {

            // öğrenci kartı
            item {
                StudentInfoCard(userProfile = userProfile)

                // araya boşluk
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "All courses",
                    color = Color(0xFF2B0B5E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                // Başlık ile ilk kart arası boşluk
                Spacer(modifier = Modifier.height(60.dp))
            }

            // dönemlere göre listeleme fall 2026
            // kaçıncı derste olduğumuzu bilmemiz için liste
            groupedCourses.entries.toList().forEachIndexed { index, entry ->
                val term = entry.key
                val courseList = entry.value

                item {
                    // dersler arası boşluk tasarımı sağlamak için
                    val topPadding = if (index == 0) 10.dp else 60.dp

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = topPadding) // Dinamik boşluk burada kullanılıyor
                            .height(50.dp), // Kartların altına girmesi için "yastık" payı
                        contentAlignment = Alignment.TopEnd // Yazıyı kutunun TEPESİNE yasla
                    ) {
                        Text(
                            text = term, // "Fall 2025"
                            color = Color(0xFF2B0B5E),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                items(courseList) { course ->
                    // ders kartı renkleri lacivert turuncu
                    val cardColor = when (courseList.indexOf(course) % 3) {
                        0 -> Color(0xFF3B5BA5)
                        1 -> Color(0xFFFF9800)
                        else -> Color(0xFF1A237E)
                    }

                    CourseCardItem(course = course, color = cardColor) {
                        navController.navigate("courseDetail/${course.title}")
                    }
                }

                // Gruplar arası ekstra boşluk
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun StudentInfoCard(userProfile: UserProfile) {
    // profil kartı
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Kartın yüksekliği
            .clip(RoundedCornerShape(32.dp)) // Köşeleri yuvarlatma
            .background(Color(0xFFDCEFA5)) // profil kartı arkaplan rengi
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profil İkonu
        Box(
            modifier = Modifier
                .size(60.dp)

                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(id = R.drawable.profile), // Senin drawable kaynağın
                contentDescription = null,
                tint = Color(0xFF2B0B5E),
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // --- YAZILAR ---
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userProfile.name, //  öğrenci adı
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2B0B5E)
            )
            Text(
                text = userProfile.department, // bölüm
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2B0B5E).copy(alpha = 0.8f)
            )
            Text(
                text = "${userProfile.studentId} / ${userProfile.degree}",
                fontSize = 12.sp,
                color = Color(0xFF2B0B5E).copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun CourseCardItem(course: Course, color: Color, onClick: () -> Unit) {
    // Ders Kartları
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(28.dp)) // Yuvarlatılmış köşeler
            .background(color)
            .clickable { onClick() }
            .padding(24.dp), // İçerik padding'i arttırıldı
        contentAlignment = Alignment.TopCenter // Yazılar üst tarafa yakın
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            // Ders Kodu
            Text(
                text = course.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Ders Adı Altı Çizgili Efekt
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ders Adı
            Text(
                text = course.subtitle,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}