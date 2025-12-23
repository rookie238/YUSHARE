package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // ViewModel'den gelen güncel gönderi listesi
    val filteredPosts = viewModel.selectedCoursePosts

    // Tasarım Renkleri
    val BackgroundColor = Color(0xFFF2F2F2)
    val HeaderTextColor = Color(0xFF2B0B5E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // --- ÜST BAŞLIK (HEADER) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 10.dp) // Status bar boşluğu
                .padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Geri",
                        tint = HeaderTextColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = courseTitle,
                    color = HeaderTextColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- İÇERİK LİSTESİ ---
        if (filteredPosts.isEmpty()) {
            // Eğer gönderi yoksa
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bu derse ait henüz yükleme yapılmamış.", color = Color.Gray)
            }
        } else {
            // Gönderiler varsa listele
            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Burada 'items' döngüsü bize her satır için bir 'post' nesnesi verir.
                // Artık manuel olarak 'val name = ...' yapmana gerek yok.
                // PostItem bileşeni zaten bu işi yapıyor.
                items(filteredPosts) { post ->

                    PostItem(
                        post = post, // Post nesnesini doğrudan aktarıyoruz
                        navController = navController
                    )

                    // Listede elemanlar arası boşluk
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}