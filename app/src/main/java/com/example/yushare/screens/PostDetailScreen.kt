package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yushare.model.Comment // Import ettiğinden emin ol
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: String,
    viewModel: SharedViewModel
) {
    // 1. Post verisini bul
    val post = remember(postId, viewModel.postsList) {
        viewModel.postsList.find { it.id == postId }
    }

    // 2. Yorumları Çek (Sayfa açılınca veya postId değişince çalışır)
    LaunchedEffect(postId) {
        viewModel.fetchComments(postId)
    }

    // Yorum Listesini ViewModel'den alıyoruz
    val comments = viewModel.commentsList

    val AppColor = Color(0xFF2B0B5E)
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {

        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = AppColor
                )
            }
            Text(
                text = "Gönderi Detayı",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor
            )
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        if (post == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gönderi bulunamadı.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // A) GÖNDERİ DETAYI
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.username.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = post.username, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = post.courseCode, color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (post.description.isNotEmpty()) {
                            Text(text = post.description, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        if (post.fileUrl.isNotEmpty()) {
                            AsyncImage(
                                model = post.fileUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
                }

                // B) YORUMLAR BAŞLIĞI
                item {
                    Text(
                        text = "Yorumlar (${comments.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                // C) DİNAMİK YORUM LİSTESİ
                if (comments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Henüz yorum yok. İlk yorumu sen yaz!", color = Color.Gray)
                        }
                    }
                } else {
                    items(comments) { comment ->
                        CommentItem(comment)
                    }
                }
            }

            // --- YORUM YAZMA ALANI ---
            Column {
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Yorum yaz...", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8F8F8), RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColor,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF8F8F8)
                        ),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                // ViewModel üzerinden yorumu gönder
                                viewModel.sendComment(postId, commentText)
                                commentText = "" // Kutuyu temizle
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(AppColor, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Gönder", tint = Color.White)
                    }
                }
            }
        }
    }
}

// --- TEKİL YORUM GÖRÜNÜMÜ ---
@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Yorum Yapanın Profil Resmi (Baş harf)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = comment.username.take(1).uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            // Kullanıcı Adı
            Text(
                text = comment.username,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            // Yorum Metni
            Text(
                text = comment.text,
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
        }
    }
}