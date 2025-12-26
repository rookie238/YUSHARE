package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.yushare.components.PostItem
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun HomeScreen(viewModel: SharedViewModel, navController: NavHostController) {
    val courses = viewModel.coursesList
    val posts = viewModel.postsList

    // --- YENİ TASARIM RENKLERİ ---
    val BackgroundColor = Color(0xFFF2F0E9) // Krem rengi arka plan
    val HeaderTextColor = Color(0xFF4A5C99) // Başlıklar için koyu mavi/gri
    val BodyTextColor = Color(0xFF1F1F1F)   // Genel metin rengi
    // ---------------------------

    LaunchedEffect(Unit) { viewModel.fetchPosts() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            // --- HEADER (For You) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 10.dp)
                    .padding(horizontal = 16.dp)
            ) {
                // A) Ortalanmış Başlık
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "For You",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeaderTextColor
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = HeaderTextColor,
                        modifier = Modifier.size(24.dp)
                    )
                }


            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- İÇERİK (Courses) ---
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // --- BAŞLIK ve SEE ALL BUTONU ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Courses",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BodyTextColor
                    )

                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        color = HeaderTextColor,
                        modifier = Modifier.clickable {
                            // "Tümünü Gör" sayfasına yönlendirme
                            navController.navigate("drafts")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- DERSLER LİSTESİ (YANA KAYAR) ---
                if (courses.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        itemsIndexed(courses) { index, course ->
                            val cardColor = if (index % 2 == 0) Color(0xFF5669AB) else Color(0xFFF19E26)

                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(cardColor)
                                    .clickable {
                                        // DERS DETAYINA GİDİŞ (Bağlantı Açıldı)
                                        navController.navigate("courseDetail/${course.title}")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = course.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                } else {
                    Text("Lessons are loading...", color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- LATEST UPLOAD BAŞLIĞI ---
                Text("Latest Upload", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BodyTextColor)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // --- GÖNDERİLER (POSTS) ---
        if (posts.isEmpty()) {
            item { Text("No uploads yet.", Modifier.padding(16.dp), color = Color.Gray) }
        } else {
            items(posts) { post ->
                // GÜNCELLEME: navController parametresi eklendi
                PostItem(post = post, navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val viewModel: SharedViewModel = viewModel()
    HomeScreen(viewModel = viewModel, navController = navController)
}