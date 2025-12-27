package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage // Eğer Coil kütüphanesi yüklü değilse, Icon kullanacağız (aşağıda açıkladım)

// --- RENK PALETİ (Görsele Uygun) ---
val BgColor = Color(0xFFF2F0E9)           // Ana arka plan (Krem)
val HighlightColor = Color(0xFFFBC176)    // Öne çıkan bildirim (Turuncu)
val DarkBlueText = Color(0xFF2B3A67)      // Başlık rengi
val BadgeRed = Color(0xFFE53935)          // Bildirim sayısı kırmızısı
val HeartColor = Color(0xFFFF6F6F)        // Kalp ikonu rengi
val GroupIconColor = Color(0xFFFF9800)    // VCD ders ikonlarının turuncusu

// --- VERİ MODELİ ---
data class NotificationItem(
    val id: Int,
    val username: String,       // Örn: "ardademir"
    val actionText: String,     // Örn: "liked your post"
    val time: String,           // Örn: "1h ago"
    val isHighlighted: Boolean, // Turuncu arka plan olsun mu?
    val type: NotificationType, // İkon tipi
    val imageUrl: String? = null // Kullanıcı resmi (yoksa baş harf veya ders kodu)
)

enum class NotificationType {
    REPLY, LIKE, MESSAGE
}

@Composable
fun NotificationScreen(navController: NavController) {
    // --- SAHTE VERİLER (Görseldekiler) ---
    val notifications = listOf(
        NotificationItem(1, "ardademir", "replied to your post", "1h ago", true, NotificationType.REPLY, "https://randomuser.me/api/portraits/men/1.jpg"),
        NotificationItem(2, "ardademir", "liked your post", "1h ago", true, NotificationType.LIKE, "https://randomuser.me/api/portraits/men/1.jpg"),
        NotificationItem(3, "serramete", "replied to your post", "2h ago", true, NotificationType.REPLY, "https://randomuser.me/api/portraits/women/2.jpg"),
        NotificationItem(4, "serramete", "liked your post", "2h ago", true, NotificationType.LIKE, "https://randomuser.me/api/portraits/women/2.jpg"),
        NotificationItem(5, "elifmansur", "liked your post", "5h ago", false, NotificationType.LIKE, "https://randomuser.me/api/portraits/women/3.jpg"),
        NotificationItem(6, "gokhanozsen", "liked your post", "7h ago", false, NotificationType.LIKE, "https://randomuser.me/api/portraits/men/4.jpg"),
        NotificationItem(7, "VCD 421", "you have a new message", "7h ago", false, NotificationType.MESSAGE),
        NotificationItem(8, "VCD 471", "you have a new message", "1d ago", false, NotificationType.MESSAGE)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(16.dp)
    ) {
        // --- HEADER KISMI ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Geri Butonu
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFDDE1E9), CircleShape) // Açık mavi/gri yuvarlak
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkBlueText
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Ortalamak için boşluk

            // Başlık ve Badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Notifications",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlueText
                )
                Spacer(modifier = Modifier.width(4.dp))
                // Kırmızı Sayı Yuvarlağı (4)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(BadgeRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "4", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1.3f)) // Görselde başlık tam ortada değil, biraz sağa yakın, dengelemek için.
        }

        // --- BİLDİRİM LİSTESİ ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { item ->
                NotificationRow(item)
            }
        }
    }
}

@Composable
fun NotificationRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)) // Oval köşeler
            .background(if (item.isHighlighted) HighlightColor else Color.Transparent) // Turuncu veya Şeffaf
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- AVATAR ALANI ---
        Box(modifier = Modifier.size(56.dp)) {
            if (item.type == NotificationType.MESSAGE) {
                // Ders Kodları için (VCD 421 gibi) Turuncu Yuvarlak
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GroupIconColor, CircleShape)
                        .border(2.dp, Color.White, CircleShape), // Beyaz çerçeve
                    contentAlignment = Alignment.Center
                ) {
                    // "VCD 421" metnini iki satıra bölüyoruz
                    val parts = item.username.split(" ")
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = parts.getOrElse(0){""}, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = parts.getOrElse(1){""}, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            } else {
                // Kişi Fotoğrafları
                // NOT: Gerçek projede 'AsyncImage' (Coil) kullanılır.
                // Eğer Coil yoksa burası gri bir yuvarlak çıkarır. Coil eklemek için build.gradle'a bakmalısın.
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // Kalp İkonu (Avatarın sağ altında)
            if (item.type == NotificationType.LIKE) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Liked",
                    tint = HeartColor,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(Color.Transparent) // Arka plan yok görselde direkt kalp var
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // --- METİN ALANI ---
        Column(modifier = Modifier.weight(1f)) {
            // İsim kalın, eylem normal (RichText)
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                        append(item.username + " ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {
                        append(item.actionText)
                    }
                },
                fontSize = 15.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Saat
            Text(
                text = item.time,
                fontSize = 13.sp,
                color = if(item.isHighlighted) Color.DarkGray else Color.Gray
            )
        }
    }
}