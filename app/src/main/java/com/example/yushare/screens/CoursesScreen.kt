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

    // --- YENİ EKLENEN KISIM: Kullanıcı profil verisi ---
    val userProfile = viewModel.currentUserProfile.value

    // Ekran açılınca kullanıcı verisini çek
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }
    // --------------------------------------------------

    // Dersleri Dönemlere Göre Grupla
    val groupedCourses = courses.groupBy { it.term }.toSortedMap() // Sıralı gelmesi için toSortedMap eklenebilir

    val BackgroundColor = Color(0xFFF9F9F4) // Krem arka plan

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // --- HEADER (Geri Butonu) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF2B0B5E))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 1. ÖĞRENCİ KARTI (Veritabanından gelen veriyi gönderiyoruz)
            item {
                StudentInfoCard(userProfile = userProfile)

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "All courses",
                    color = Color(0xFF2B0B5E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 2. DÖNEMLERE GÖRE LİSTELEME
            groupedCourses.forEach { (term, courseList) ->
                item {
                    // Dönem Başlığı (Sağda)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = term, // "Fall 2025" veritabanından gelecek
                            color = Color(0xFF2B0B5E),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                items(courseList) { course ->
                    // Renk Mantığı
                    val cardColor = when (courseList.indexOf(course) % 3) {
                        0 -> Color(0xFF3B5BA5) // Koyu Mavi
                        1 -> Color(0xFFFF9800) // Turuncu
                        else -> Color(0xFFE53935) // Kırmızı
                    }

                    CourseCardItem(course = course, color = cardColor) {
                        navController.navigate("courseDetail/${course.title}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun StudentInfoCard(userProfile: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE0EFA5)) // Açık yeşil/sarı tonu
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profil İkonu
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(1.dp, Color(0xFF2B0B5E), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(painter = painterResource(id = R.drawable.profile), contentDescription = null, tint = Color(0xFF2B0B5E))
        }

        Spacer(modifier = Modifier.width(12.dp))

        // --- DİNAMİK VERİ KISMI ---
        Column {
            // İsim Soyisim (Database: name)
            Text(
                text = userProfile.name,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B0B5E)
            )
            // Bölüm (Database: department)
            Text(
                text = userProfile.department,
                fontSize = 12.sp,
                color = Color(0xFF2B0B5E)
            )
            // Okul No / Derece (Database: id / degree)
            Text(
                text = "${userProfile.studentId}/ ${userProfile.degree}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CourseCardItem(course: Course, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp)) // Köşeleri yuvarlatılmış
            .background(color)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = course.title, // COMM 101
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = course.subtitle, // Introduction to...
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}