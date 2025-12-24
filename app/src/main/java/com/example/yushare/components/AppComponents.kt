package com.example.yushare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yushare.R
import com.example.yushare.model.Course
import com.example.yushare.model.Post
import kotlinx.coroutines.launch

@Composable
fun CourseCarousel(courses: List<Course>, navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { courses.size })
    val scope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }, enabled = pagerState.currentPage > 0) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = if(pagerState.currentPage > 0) Color(0xFF5D1EC6) else Color.LightGray)
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).height(120.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            pageSpacing = 16.dp
        ) { page ->
            val course = courses[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD9D9D9), RoundedCornerShape(16.dp))
                    .clickable { navController.navigate("courseDetail/${course.title}") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(course.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2B0B5E))
                    if(course.subtitle.isNotEmpty()) Text(course.subtitle, fontSize = 12.sp)
                }
            }
        }

        IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }, enabled = pagerState.currentPage < courses.size - 1) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = if(pagerState.currentPage < courses.size - 1) Color(0xFF5D1EC6) else Color.LightGray)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF5F5F7), RoundedCornerShape(16.dp))
            .padding(bottom = 12.dp)
    ) {
        // --- ÜST BİLGİ ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Kullanıcı Adı
            // ViewModel zaten "@melisakosun" şeklinde kaydettiği için buraya tekrar @ koymuyoruz.
            Text(
                text = post.username,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            // 2. Ders Adı
            Text(
                text = post.courseCode,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF2B0B5E)
            )
        }

        // --- İÇERİK KISMI ---
        // 1. Eğer Resimse
        if (post.fileType == "image" && post.fileUrl.isNotEmpty()) {
            AsyncImage(
                model = post.fileUrl,
                contentDescription = "Upload",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
        }
        // 2. Eğer Diğer Dosya Türüyle (PDF, Ses)
        else if (post.fileUrl.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when {
                    post.fileType == "pdf" -> Icons.Default.Description
                    post.fileType == "audio" -> Icons.Default.Audiotrack
                    else -> Icons.Default.InsertDriveFile
                }

                Icon(icon, null, tint = Color(0xFF2B0B5E), modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Attached File", fontWeight = FontWeight.Bold)
                    Text(post.fileType.uppercase(), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // Açıklama / Yorum Gösterimi
        if (post.description.isNotEmpty()) {
            Text(
                text = post.description,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ChipItem(text: String) {
    Box(modifier = Modifier.background(Color(0xFFE0E0E0), RoundedCornerShape(20.dp)).padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(text, fontSize = 14.sp, color = Color(0xFF2B0B5E), fontWeight = FontWeight.Medium)
    }
}