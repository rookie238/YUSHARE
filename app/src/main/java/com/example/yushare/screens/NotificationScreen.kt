package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

// --- Model Sınıfı (ViewModel olmadığı için buraya ekledim) ---
data class NotificationItem(
    val fromUserName: String,
    val message: String,
    val actionType: String, // "LIKE", "REPLY", "MESSAGE" vb.
    val timestamp: Long
)

// --- Renk Paleti ---
val BgBeige = Color(0xFFF1F0EA)
val CardOrange = Color(0xFFF8C67B)
val HeaderBlue = Color(0xFF203B82)
val BadgeRed = Color(0xFFEB3436)
val BackBtnBg = Color(0xFFD6DCE7)
val VcdOrange = Color(0xFFFF9800)

@Composable
fun NotificationsScreen(
    navController: NavController
    // ViewModel parametresi kaldırıldı
) {
    // --- SABİT VERİLER (MOCK DATA) ---
    val now = System.currentTimeMillis()
    val oneHour = 3600000L
    val oneDay = 86400000L

    val notifications = listOf(
        NotificationItem(
            fromUserName = "ardademir",
            message = "replied to your post",
            actionType = "REPLY",
            timestamp = now - oneHour
        ),
        NotificationItem(
            fromUserName = "ardademir",
            message = "liked your post",
            actionType = "LIKE",
            timestamp = now - oneHour
        ),
        NotificationItem(
            fromUserName = "serramete",
            message = "replied to your post",
            actionType = "REPLY",
            timestamp = now - (oneHour * 2)
        ),
        NotificationItem(
            fromUserName = "serramete",
            message = "liked your post",
            actionType = "LIKE",
            timestamp = now - (oneHour * 2)
        ),
        NotificationItem(
            fromUserName = "elifmansur",
            message = "liked your post",
            actionType = "LIKE",
            timestamp = now - (oneHour * 5)
        ),
        NotificationItem(
            fromUserName = "gokhanozsen",
            message = "liked your post",
            actionType = "LIKE",
            timestamp = now - (oneHour * 7)
        ),
        NotificationItem(
            fromUserName = "VCD 421",
            message = "you have a new message",
            actionType = "MESSAGE",
            timestamp = now - (oneHour * 7)
        ),
        NotificationItem(
            fromUserName = "VCD 471",
            message = "you have a new message",
            actionType = "MESSAGE",
            timestamp = now - oneDay
        )
    )

    // İçeriği çiziyoruz
    NotificationsContent(
        notifications = notifications,
        onBackClick = { navController.popBackStack() }
    )
}

// --- UI Çizen Fonksiyon ---
@Composable
fun NotificationsContent(
    notifications: List<NotificationItem>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBeige)
            .statusBarsPadding()
    ) {
        // --- BAŞLIK ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BackBtnBg)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = HeaderBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Notifications",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = HeaderBlue
            )

            // Bildirim Sayısı Rozeti
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(BadgeRed),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notifications.size.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1.3f))
        }

        // --- LİSTE ---
        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(notifications) { index, item ->
                    // İlk 4 öğeyi turuncu (CardOrange), diğerlerini şeffaf yap
                    val isHighlighted = index < 4
                    NotificationRow(item, isHighlighted)
                }
            }
        }
    }
}

@Composable
fun NotificationRow(item: NotificationItem, isHighlighted: Boolean) {
    val backgroundColor = if (isHighlighted) CardOrange else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Profil Alanı ---
        Box(modifier = Modifier.size(56.dp)) {
            // "VCD" ile başlayan kullanıcılar için özel ikon
            if (item.fromUserName.startsWith("VCD")) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(VcdOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.fromUserName.replace(" ", "\n"),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            } else {
                // Diğer kullanıcılar için Avatar
                AsyncImage(
                    model = "https://i.pravatar.cc/150?u=${item.fromUserName}",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }

            // Beğeni (Like) ikonu varsa sağ alta ekle
            if (item.actionType == "LIKE") {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // --- Metin Alanı ---
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)) {
                        append(item.fromUserName)
                    }
                    append(" ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp)) {
                        append(item.message)
                    }
                },
                color = Color.Black
            )
            Text(
                text = getTimeAgo(item.timestamp),
                fontSize = 13.sp,
                color = if (isHighlighted) Color.DarkGray else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Zaman hesaplama fonksiyonu
fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val hours = (diff / (1000 * 60 * 60)).toInt()
    val days = (diff / (1000 * 60 * 60 * 24)).toInt()

    return when {
        hours < 1 -> "Just now"
        hours < 24 -> "${hours}h ago"
        else -> "${days}d ago"
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    val navController = rememberNavController()
    NotificationsScreen(navController = navController)
}