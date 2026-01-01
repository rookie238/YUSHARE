package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yushare.model.NotificationItem
import com.example.yushare.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    // ViewModel'deki canlı bildirim listesi
    val notifications = viewModel.notificationsList
    val AppColor = Color(0xFF2B0B5E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding() // Üst barın altında kalmaması için
    ) {
        // --- BAŞLIK ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                text = "Bildirimler",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor
            )
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        // --- LİSTE ---
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Henüz bildirim yok.", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(notifications) { item ->
                    NotificationRow(item)
                }
            }
        }
    }
}

@Composable
fun NotificationRow(item: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profil Resmi (Sembolik Baş Harf)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.fromUserName.take(1).uppercase(),
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Bildirim Metni
            Text(
                text = "${item.fromUserName} ${item.message}", // Örn: Ahmet liked your post
                fontSize = 14.sp,
                color = Color.Black
            )
            // Zaman
            Text(
                text = formatTimestamp(item.timestamp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // İkon (Beğeni veya Bilgi)
        Icon(
            imageVector = if (item.actionType == "LIKE") Icons.Default.Favorite else Icons.Default.Info,
            contentDescription = null,
            tint = if (item.actionType == "LIKE") Color.Red else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
    HorizontalDivider(color = Color(0xFFF5F5F5))
}

// Zaman damgasını okunabilir formata çeviren yardımcı fonksiyon
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}