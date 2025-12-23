package com.example.yushare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.yushare.R // R dosyasının import edildiğinden emin ol
import com.example.yushare.ui.theme.* // Color dosyasını import et


@Composable
fun CreateGroupScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMessaging: () -> Unit
) {
    // Input metnini tutacak state
    var groupName by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { YushareBottomNavigation() }, // Aynı alt barı kullanıyoruz
        containerColor = YuBackground // Bej rengi arka plan
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Üst Bar (Header)
            CreateGroupTopBar(onBackClick = onNavigateBack)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Input Alanı ve Next Butonu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Özel Text Field (Yuvarlak Gri Kutu)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFD9DCE3)) // Görseldeki input arkaplan rengi (hafif gri-mavi)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (groupName.isEmpty()) {
                        Text(
                            text = "Group name",
                            color = YuMediumBlue.copy(alpha = 0.4f), // Placeholder rengi
                            fontSize = 16.sp
                        )
                    }
                    BasicTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        textStyle = TextStyle(
                            color = YuMediumBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Next Butonu (Sadece Yazı)
                Text(
                    text = "Next",
                    color = YuMediumBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        // Grubu kurma mantığı buraya eklenebilir
                        onNavigateToMessaging()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Choose Participants Alanı (Büyük Konteyner)
            // Kalan tüm alanı kaplaması için weight(1f) kullanıyoruz
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ekranın altına kadar uzat
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) // Üst köşeleri geniş yuvarlat
                    .background(Color(0xFFD2D6DE)) // Görseldeki büyük alanın rengi (Paletteki griye yakın)
            ) {
                Column(
                    modifier = Modifier.padding(top = 32.dp, start = 32.dp)
                ) {
                    Text(
                        text = "Choose Participants",
                        color = YuMediumBlue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Buraya katılımcı listesi (LazyColumn vb.) gelecek
                    // Şimdilik görseldeki gibi boş bırakıyoruz.
                }
            }
        }
    }
}

// ==========================================
// BİLEŞENLER (COMPONENTS)
// ==========================================

@Composable
fun CreateGroupTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Geri Butonu
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFDBDEE7)) // YuLightBlueBg
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = YuMediumBlue
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Başlık
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Groups",
                color = YuMediumBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select",
                tint = YuMediumBlue,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1.2f)) // Optik ortalama için
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true)
@Composable
fun CreateGroupScreenPreview() {
    CreateGroupScreen(
        onNavigateBack = {},
        onNavigateToMessaging = {}
    )
}