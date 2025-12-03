package com.example.yushare.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun CourseDetailScreen(courseTitle: String, viewModel: SharedViewModel, navController: NavHostController) {
    LaunchedEffect(courseTitle) {
        viewModel.fetchPostsByCourse(courseTitle)
    }

    val filteredPosts = viewModel.selectedCoursePosts

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF5D1EC6)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color.White)
                }
                Text(text = courseTitle, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (filteredPosts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bu derse ait henüz yükleme yapılmamış.", color = Color.Gray)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(filteredPosts) { post ->
                    PostItem(post)
                }
            }
        }
    }
}