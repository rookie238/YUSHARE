package com.example.yushare

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(courseId: Int, onBackClick: () -> Unit) {
    // Navigasyondan gelen ID ile sampleCourses listesinden ilgili kursu bulur.
    // Eğer kurs bulunamazsa ekranı boş döndürür (return).
    val course = sampleCourses.find { it.id == courseId } ?: return

    // Yorum yazma alanındaki metni tutan state (durum) değişkeni.
    var commentText by remember { mutableStateOf("") }

    // Kursun rengine göre başlık (Header) arka plan rengini belirler.
    val headerColor = when(course.colorType) {
        CourseColor.BLUE -> Color(0xFF2B3A8C)
        CourseColor.ORANGE -> Color(0xFFFF9800)
        CourseColor.LIME -> Color(0xFFD7F171)
    }


    val headerTextColor = if(course.colorType == CourseColor.LIME) Color(0xFF2B3A8C) else Color.White

    Scaffold(
        containerColor = AppBg, // Genel arka plan rengi
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    // Geri dönüş butonu: Geri tuşuna basıldığında onBackClick fonksiyonunu çağırır.
                    IconButton(onClick = onBackClick) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD7F171),
                            modifier = Modifier.size(45.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFF2B3A8C),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
        ) {

            // --- DERS BAŞLIK KARTI ---
            // Ders kodu ve tam adının göründüğü renkli ana kart.
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = headerColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(course.code, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = headerTextColor)
                    Text(course.name, fontSize = 16.sp, color = headerTextColor)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- EĞİTMEN BÖLÜMÜ ---
            Text("Lecturer", fontWeight = FontWeight.Bold, color = Color(0xFF2B3A8C), fontSize = 16.sp)
            Text(course.lecturer, fontSize = 15.sp, color = Color(0xFF2B3A8C))

            Spacer(modifier = Modifier.height(16.dp))

            // --- BİLGİ (INFO) BÖLÜMÜ ---
            // İçerisine daha sonra metin veya detay gelebilecek beyaz kutu.
            Text("Info", fontWeight = FontWeight.Bold, color = Color(0xFF2B3A8C), fontSize = 16.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .border(1.dp, Color(0xFF2B3A8C).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- MEVCUT YORUM KARTI ---
            // Sabit bir örnek yorumu ve puanlamayı gösteren lacivert kart.
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B3A8C)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Very helpful course, would highly recommend!",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("11", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("⭐⭐⭐⭐⭐", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- YORUM YAPMA ALANI ---
            // Kullanıcının metin girebileceği ve "Send" butonuyla gönderebileceği alan.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFF2B3A8C), RoundedCornerShape(16.dp))
            ) {
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = {
                        Text("Make a comment...", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Gönder Butonu: Tıklandığında metin kutusunu temizler.
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clickable {
                            if (commentText.isNotBlank()) {
                                commentText = ""
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1A237E)
                ) {
                    Text(
                        "Send",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}