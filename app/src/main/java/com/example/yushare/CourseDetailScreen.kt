package com.example.yushare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    onBackClick: () -> Unit
) {
    // ID'ye göre dersi buluyoruz
    val course = sampleCourses.find { it.id == courseId } ?: sampleCourses.first()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Text(course.code, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(course.name, color = Color.White, fontSize = 12.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF310078))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F5F0))
                .padding(16.dp)
        ) {
            // Lecturer Info
            Text("Lecturer", fontWeight = FontWeight.Bold, color = Color(0xFF310078))
            Text(course.lecturer, modifier = Modifier.padding(bottom = 16.dp))

            // Info Box
            Text("Info", fontWeight = FontWeight.Bold, color = Color(0xFF310078))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD0D0D0), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(course.description)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Review Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Very helpful course, would highly recommend!", color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbs up icon here
                        Text("👍 11", fontWeight = FontWeight.Bold)

                        // Stars
                        Row {
                            repeat(5) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comment Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("Make a comment...", color = Color.Gray)
                Button(
                    onClick = { /* Gönder işlemi */ },
                    modifier = Modifier.align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF310078))
                ) {
                    Text("Send")
                }
            }
        }
    }
}