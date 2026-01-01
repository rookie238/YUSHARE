package com.example.yushare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder // İçi boş kalp ikonu için gerekli
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.yushare.model.Post

@Composable
fun PostItem(post: Post, navController: NavController) {

    val imageUrl: String = post.fileUrl ?: ""
    val content: String = post.description ?: ""
    val userName: String = post.username ?: "Anonim"
    val timeAgo: String = "1s önce"

    // --- BEĞENİ DURUMU ---
    // false = Beğenilmemiş (İçi boş)
    // true  = Beğenilmiş (Kırmızı ve dolu)
    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Üst Kısım: Profil ve İsim
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = timeAgo, color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Açıklama
        if (content.isNotEmpty()) {
            Text(text = content, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Resim
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // --- BUTONLAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. BEĞENİ BUTONU (GÜNCELLENDİ)
            Icon(
                // Eğer beğenildiyse DOLU KALP, değilse BOŞ KALP göster
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,

                contentDescription = "Like",

                // Eğer beğenildiyse KIRMIZI, değilse GRİ yap
                // (Not: Beyaz yaparsak arka plan da beyaz olduğu için ikon kaybolur, o yüzden gri/siyah çerçeve kullanıyoruz)
                tint = if (isLiked) Color.Red else Color.Gray,

                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        isLiked = !isLiked // Durumu tersine çevir
                    }
            )

            // 2. YORUM BUTONU
            Icon(
                imageVector = Icons.Outlined.Comment,
                contentDescription = "Comment",
                tint = Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.navigate("post_detail/${post.id}")
                    }
            )
        }
    }
}