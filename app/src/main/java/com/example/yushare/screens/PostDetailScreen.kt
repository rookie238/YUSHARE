package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.yushare.viewmodel.SharedViewModel

@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: String,
    viewModel: SharedViewModel
) {
    // 1. ViewModel'deki listeden ID'si eşleşen postu buluyoruz
    // remember key olarak postId verdik, eğer id değişirse tekrar bulur.
    val post = remember(postId, viewModel.postsList) {
        viewModel.postsList.find { it.id == postId }
    }

    // Uygulamanın ana rengi
    val AppColor = Color(0xFF2B0B5E)

    // Yorum metnini tutan değişken
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {

        // --- 1. ÜST KISIM (Header) ---
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

        // Eğer post bulunamazsa (yüklenirken veya hata olursa)
        if (post == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gönderi bulunamadı veya silinmiş.")
            }
        } else {
            // Post bulunduysa içeriği göster
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                // A) GÖNDERİNİN KENDİSİ
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Profil Kısmı
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Profil resmi placeholder (Gerçek resim varsa AsyncImage kullanılabilir)
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
                                // Modelinde timestamp yoksa courseCode gösterebiliriz
                                Text(text = post.courseCode, color = Color.Gray, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Açıklama
                        if (post.description.isNotEmpty()) {
                            Text(text = post.description, fontSize = 15.sp, lineHeight = 20.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Resim (Varsa)
                        if (post.fileUrl.isNotEmpty()) {
                            AsyncImage(
                                model = post.fileUrl,
                                contentDescription = "Post Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Beğeni (Statik)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Beğeniler", color = Color.Gray, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
                }

                // B) YORUMLAR BAŞLIĞI
                item {
                    Text(
                        text = "Yorumlar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                // C) YORUM LİSTESİ (Burada Post'a ait yorumları çekmek gerekecek)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Henüz yorum yok.\nİlk yorumu sen yaz!",
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // --- 3. ALT KISIM (Yorum Yazma Alanı) ---
            Column {
                HorizontalDivider(color = Color(0xFFEEEEEE))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .imePadding(), // Klavye açılınca yukarı kayması için
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
                                // TODO: Yorumu veritabanına kaydetme işlemi buraya gelecek
                                println("Yorum gönderildi: $commentText (Post ID: $postId)")
                                commentText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(AppColor, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Gönder",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}