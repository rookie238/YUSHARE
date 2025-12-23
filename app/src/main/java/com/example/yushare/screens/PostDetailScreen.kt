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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@Composable
fun PostDetailScreen(navController: NavController) {

    // --- MOCK DATA (İleride burası veritabanından gelecek) ---
    val username = "Kullanıcı Adı"
    val timeAgo = "2s önce"
    val description = "Bu gönderinin açıklaması burada yer alacak. Kullanıcı ne paylaştıysa detayları burada göreceğiz."
    // Test için rastgele bir resim (Boşsa göstermez)
    val imageUrl = "https://picsum.photos/600/400"

    // Yorum metnini tutan değişken
    var commentText by remember { mutableStateOf("") }

    // Uygulamanın ana rengi (Mor)
    val AppColor = Color(0xFF2B0B5E)

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
                text = "Gönderi",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor
            )
        }

        Divider(color = Color(0xFFEEEEEE))

        // --- 2. ORTA KISIM (Kaydırılabilir Alan) ---
        // LazyColumn kullanarak içeriğin kaydırılmasını sağlıyoruz.
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            // A) GÖNDERİNİN KENDİSİ (Listenin en üstü)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Profil Kısmı
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = username, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = timeAgo, color = Color.Gray, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Açıklama
                    if (description.isNotEmpty()) {
                        Text(text = description, fontSize = 15.sp, lineHeight = 20.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Resim (Varsa)
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Post Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp) // Biraz daha büyük gösterelim
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Beğeni sayısı vs (Görsel olarak)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("12 Beğeni", color = Color.Gray, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFEEEEEE))
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

            // C) YORUM LİSTESİ (Şimdilik Boş/Placeholder)
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
            Divider(color = Color(0xFFEEEEEE))

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

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun PostDetailScreenPreview() {
    val navController = rememberNavController()
    PostDetailScreen(navController = navController)
}