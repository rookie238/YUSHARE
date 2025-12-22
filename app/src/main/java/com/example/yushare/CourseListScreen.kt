package com.example.yushare

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun CourseListScreen(
    onCourseClick: (Int) -> Unit, // Bir kursa tıklandığında çalışacak fonksiyon
    onBackClick: () -> Unit      // Geri butonuna tıklandığında çalışacak fonksiyon
) {
    Scaffold(containerColor = Color(0xFFF3F4ED)) { padding ->
        // Dersleri "semester" (dönem) alanına göre gruplandırır (Örn: "Fall 2025" -> [Dersler])
        val grouped = sampleCourses.groupBy { it.semester }
        val semesterEntries = grouped.entries.toList()

        // Verimli liste gösterimi sağlayan yapı
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Listenin en başındaki sabit öğeler
            item {
                Spacer(modifier = Modifier.height(16.dp))
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.height(16.dp))
                UserCardNew() // Profil bilgileri kartı
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Her bir dönemi ve o döneme ait dersleri döngüye alır
            semesterEntries.forEachIndexed { semesterIndex, (semester, courses) ->
                // Dönem Başlığı (Örn: All courses - Fall 2025)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "All courses",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B3A8C),
                            fontSize = 15.sp
                        )
                        Text(
                            semester,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B3A8C),
                            fontSize = 15.sp
                        )
                    }
                }

                // O döneme ait ders kartlarını listeler
                itemsIndexed(courses) { index, course ->
                    // Kartların birbirini biraz kapatması (overlap) için ayrılan alan (işe yaramıyor)

                    val stepHeight = 72.dp
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(stepHeight)
                            // zIndex: Üstteki kartın alttakinin üstünde görünmesini sağlar.
                            .zIndex((courses.size - index).toFloat())
                    ) {
                        CourseCard(course) { onCourseClick(course.id) }
                    }
                }

                // Dönemler arası boşluk ve ayırıcı çizgi
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    if (semesterIndex < semesterEntries.size - 1) {
                        // Dönemler arasına turuncu ince bir çizgi çeker
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            thickness = 1.5.dp,
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }
            // Listenin en altına ekstra boşluk
            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}

/**
 * Derslerin listede göründüğü kart tasarımı
 */
@Composable
fun CourseCard(course: Course, onClick: () -> Unit) {
    // Veri modelindeki CourseColor'a göre arka plan rengini belirler
    val bgColor = when(course.colorType) {
        CourseColor.BLUE -> Color(0xFF2B3A8C)
        CourseColor.ORANGE -> Color(0xFFFF9800)
        CourseColor.LIME -> Color(0xFFD7F171)
    }
    // Lime rengi için koyu, diğerleri için beyaz metin rengi ayarlar
    val textColor = if(course.colorType == CourseColor.LIME) Color(0xFF2B3A8C) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp) // Kartın yüksekliği
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(30.dp)),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = course.code,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = course.name,
                fontSize = 13.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Üst kısımdaki kullanıcı bilgilerini içeren yeşil profil kartı
 */
@Composable
fun UserCardNew() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFD7F171))
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = Color(0xFF2B3A8C)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Arda Demir", fontWeight = FontWeight.Bold, color = Color(0xFF2B3A8C), fontSize = 17.sp)
            Text("Visual Communication Design", color = Color(0xFF2B3A8C), fontSize = 13.sp)
            Text("2021060453/ Bachelor", color = Color(0xFF2B3A8C).copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

/**
 * Sol üstte bulunan yuvarlak geri dönüş butonu
 */
@Composable
fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color(0xFFD7F171))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            // Sağdan sola yazılan dillerde otomatik yön değiştiren ikon tipi
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF2B3A8C),
            modifier = Modifier.size(24.dp)
        )
    }
}