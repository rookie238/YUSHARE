package com.example.yushare.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R // R dosyasının import edildiğinden emin ol
import com.example.yushare.ui.theme.* // Color dosyasını import et
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GroupsScreen(
    onNavigateToCreateGroup: () -> Unit,
    onNavigateToChat: (String) -> Unit
) {
    Scaffold(

        containerColor = YuBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Üst Bar (Header)
            GroupsTopBar()

            // Scroll Edilebilir İçerik
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 100.dp), // Alt barın altında kalmaması için
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // --- All Groups Başlık ---
                item {
                    SectionTitle(text = "All Groups")
                }

                // --- Yatay Liste (Horizontal Scroll) ---
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal =25.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            BigGroupCard(
                                title = "UI Design",
                                code = "VCD471",
                                tag = "Design",
                                color = YuOrange,
                                onClick = { onNavigateToChat("UI Design Study Group")}
                            )
                        }
                        item {
                            BigGroupCard(
                                title = "Kotlin 101",
                                code = "CS101",
                                tag = "Code",
                                color = YuMediumBlue,
                                onClick = { onNavigateToChat("Kotlin 101 Study Group") }
                            )
                        }
                    }
                }

                // --- My Groups Başlık ---
                item {
                    SectionTitle(text = "My Groups")
                }

                // --- Dikey Liste (My Groups Stack) ---
                item {
                    MyGroupsStack(onGroupClick = onNavigateToChat)
                }

                // --- Create Butonu ---
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = onNavigateToCreateGroup,
                            colors = ButtonDefaults.buttonColors(containerColor = YuLime),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .width(200.dp)
                                .height(56.dp)
                        ) {
                            Text(
                                text = "CREATE",
                                color = YuMediumBlue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// BİLEŞENLER (COMPONENTS)
// ==========================================

@Composable
fun GroupsTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Geri Butonu

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
                tint = YuMediumBlue
            )
        }

        Spacer(modifier = Modifier.weight(1.2f))
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = YuMediumBlue,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun BigGroupCard(title: String, code: String, tag: String, color: Color,onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Surface(
                color = YuMediumBlue,
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
                    Text(text = tag, color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = if (color == YuOrange) YuMediumBlue else Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            )
            Text(
                text = code,
                color = if (color == YuOrange) YuMediumBlue.copy(alpha=0.7f) else Color.White.copy(alpha=0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun MyGroupsStack(onGroupClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy((-12).dp) // Kartların üst üste binmesi için
    ) {
        CompactGroupCard(
            code = "COMM 101", title = "Introduction to Communication",
            color = YuMediumBlue, textColor = YuLime, isTop = true,
                    onClick = { onGroupClick("COMM 101 Study Group") }
        )
        CompactGroupCard(
            code = "COMM 171", title = "Intro to Communication Design",
            color = YuOrange, textColor = Color.White,
                    onClick = { onGroupClick("COMM 171 Study Group") }
        )
        CompactGroupCard(
            code = "VCD 111", title = "Basic Drawing",
            color = YuMediumBlue, textColor = YuLime,
                    onClick = { onGroupClick("VCD 111 Study Group") }
        )
        CompactGroupCard(
            code = "COMM 199", title = "Seminar in Academic Writing Skills",
            color = YuOrange, textColor = Color.White, isBottom = true,
                    onClick = { onGroupClick("COMM 199 Study Group") }
        )
    }
}

@Composable
fun CompactGroupCard(
    code: String, title: String, color: Color, textColor: Color,
    isTop: Boolean = false, isBottom: Boolean = false, onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(90.dp).clickable { onClick() },
        shape = RoundedCornerShape(
            topStart = 24.dp, topEnd = 24.dp,
            bottomStart = if(isBottom) 24.dp else 8.dp,
            bottomEnd = if(isBottom) 24.dp else 8.dp
        ),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = code, color = textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = title, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp, maxLines = 1)
        }
    }
}

// ==========================================
// CUSTOM BOTTOM NAVIGATION (DRAWABLE KAYNAKLI)
// ==========================================

@Composable
fun NavIcon(@DrawableRes iconId: Int, isSelected: Boolean) {
    Icon(
        painter = painterResource(id = iconId),
        contentDescription = null,
        tint = if (isSelected) Color.White else YuGray,
        modifier = Modifier.size(28.dp)
    )
}
@Preview(showBackground = true)
@Composable
fun GroupsScreenPreview() {
    GroupsScreen(onNavigateToCreateGroup = {},
                 onNavigateToChat = {})
}