package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yushare.viewmodel.SharedViewModel

// Yorum verilerini tutmak için veri sınıfı
data class ReviewData(
    val text: String,
    val likes: String = "0",
    val dislikes: String = "0",
    val stars: Int = 5
)

@Composable
fun CourseDetailScreen(
    courseTitle: String,
    viewModel: SharedViewModel,
    navController: NavHostController
) {
    // Yorum metnini tutacak değişken
    var commentText by remember { mutableStateOf("") }

    // Seçilen yıldız sayısını tutacak değişken (Varsayılan 5)
    var currentRating by remember { mutableIntStateOf(5) }

    // Ekranda gösterilecek yorumların listesi
    val reviewsList = remember { mutableStateListOf(
        ReviewData(
            text = "Very helpful course, would highly recommend!",
            likes = "11",
            dislikes = "0",
            stars = 5
        )
    )}

    LaunchedEffect(courseTitle) {
        viewModel.fetchPostsByCourse(courseTitle)
    }

    val currentCourse = viewModel.coursesList.find { it.title == courseTitle }

    val BackgroundColor = Color(0xFFF9F9F4)
    val ReviewBlueColor = Color(0xFF2D4295)
    val LimeGreenColor = Color(0xFFC0CA33)
    val ButtonPurpleColor = Color(0xFF2B0B5E)
    val StarColor = Color(0xFFFFC107) // Amber/Sarı
    val UnselectedStarColor = Color.Gray.copy(alpha = 0.5f) // Sönük yıldız rengi

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
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 1. DERS BİLGİ KARTI
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF9800))
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

            // 2. EĞİTMEN VE AÇIKLAMA
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

            // 3. DEĞERLENDİRME LİSTESİ
            items(reviewsList) { review ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReviewBlueColor)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = review.text,
                            color = LimeGreenColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Sol taraf: Like/Dislike
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Like",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = review.likes,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.ThumbDown,
                                    contentDescription = "Dislike",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Sağ taraf: Yıldızlar
                            Row {
                                repeat(5) { index ->
                                    // Eğer index (0'dan başlar) < yıldız sayısı ise dolu/renkli göster
                                    val starTint = if (index < review.stars) StarColor else UnselectedStarColor
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star",
                                        tint = starTint,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. YORUM YAPMA ALANI (GÜNCELLENEN KISIM)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReviewBlueColor)
                        .padding(16.dp)
                ) {
                    // Yazı Giriş Alanı
                    BasicTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 40.dp)
                            .align(Alignment.TopStart),
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            if (commentText.isEmpty()) {
                                Text(
                                    text = "Make a comment...",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    // --- YILDIZ SEÇME ALANI (SOL ALT) ---
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1'den 5'e kadar yıldızları oluşturuyoruz
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$i Star",
                                // Seçilen puana eşit veya küçükse Sarı, değilse Gri
                                tint = if (i <= currentRating) StarColor else UnselectedStarColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { currentRating = i } // Tıklayınca puanı güncelle
                                    .padding(end = 4.dp)
                            )
                        }
                    }

                    // --- GÖNDER BUTONU (SAĞ ALT) ---
                    Button(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                reviewsList.add(
                                    ReviewData(
                                        text = commentText,
                                        likes = "0",
                                        dislikes = "0",
                                        stars = currentRating // Seçilen yıldızı kaydet
                                    )
                                )
                                commentText = "" // Kutuyu temizle
                                currentRating = 5 // Yıldızı varsayılana döndür
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonPurpleColor),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .height(36.dp)
                    ) {
                        Text(
                            text = "Send",
                            color = LimeGreenColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}