package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yushare.components.PostItem
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun CourseDetailScreen(
    courseTitle: String,
    viewModel: SharedViewModel,
    navController: NavHostController
) {
    // 1. Ekran açılınca o derse ait gönderileri çek
    LaunchedEffect(courseTitle) {
        viewModel.fetchPostsByCourse(courseTitle)
    }

    // Seçilen dersin tüm bilgilerini bul (Eğitmen, açıklama vs için)
    val currentCourse = viewModel.coursesList.find { it.title == courseTitle }
    val filteredPosts = viewModel.selectedCoursePosts

    val BackgroundColor = Color(0xFFF9F9F4) // Tasarımdaki krem arka plan

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, bottom = 10.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color(0xFF2B0B5E))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), // Kenarlardan boşluk
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 1. DERS BİLGİ KARTI (Turuncu Kart)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF9800)) // Tasarımdaki turuncu
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentCourse?.title ?: courseTitle,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = currentCourse?.subtitle ?: "",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 2. EĞİTMEN VE AÇIKLAMA (Lecturer & Info)
            item {
                Text(
                    text = "Lecturer",
                    color = Color(0xFF2B0B5E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = currentCourse?.lecturer ?: "Loading...",
                    color = Color(0xFF2B0B5E),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Info",
                    color = Color(0xFF2B0B5E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Açıklama Kutusu (Beyaz kutu)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .border(1.dp, Color(0xFF5E5E99), RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentCourse?.description ?: "No description available.",
                        color = Color(0xFF2B0B5E),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }


        }
    }
}