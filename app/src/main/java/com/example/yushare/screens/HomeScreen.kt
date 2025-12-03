package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview // EKLENDİ
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // EKLENDİ
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController // EKLENDİ
import com.example.yushare.R
import com.example.yushare.components.ChipItem
import com.example.yushare.components.CourseCarousel
import com.example.yushare.components.PostItem
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun HomeScreen(viewModel: SharedViewModel, navController: NavHostController) {
    val courses = viewModel.coursesList
    val posts = viewModel.postsList

    LaunchedEffect(Unit) { viewModel.fetchPosts() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth().height(85.dp).background(Color(0xFF5D1EC6)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- BURASI DÜZENLENDİ ---
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape) // 1. ÖNEMLİ: Tıklama efekti yuvarlak olsun diye şekli kesiyoruz
                            .background(Color.White)
                            .clickable {
                                // 2. Tıklanınca nereye gidecek? (Genelde Profil sayfasına)
                                navController.navigate("profile")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.avatar_placeholder),
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                    }
                    // --------------------------

                    // --- ZİL BUTONU ---
                    IconButton(
                        onClick = {
                            // "notifications" rotasına git
                            navController.navigate("notifications")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Bildirimler",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Popular Courses
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Popular Courses", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                if (courses.isNotEmpty()) {
                    CourseCarousel(courses, navController)
                } else {
                    Text("Dersler yükleniyor...", color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Group & Help", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ChipItem("Maya")
                    ChipItem("VCD311")
                }
                Spacer(modifier = Modifier.height(25.dp))
                Text("Latest Upload", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (posts.isEmpty()) {
            item { Text("Henüz yükleme yok.", Modifier.padding(16.dp), color = Color.Gray) }
        } else {
            items(posts) { post -> PostItem(post) }
        }
    }
}

// --- EKLENEN PREVIEW KISMI ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    // ViewModel'i preview içinde oluşturuyoruz.
    // Not: Firebase bağlantısı olduğu için preview'da veriler boş gelebilir veya hata verebilir,
    // ancak tasarım şablonunu görebilirsin.
    val viewModel: SharedViewModel = viewModel()

    HomeScreen(viewModel = viewModel, navController = navController)
}