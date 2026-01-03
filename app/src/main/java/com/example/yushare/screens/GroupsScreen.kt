package com.example.yushare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// --- Tasarım Renkleri ---
private val DesignOrange = Color(0xFFFF9800)
private val DesignBlue = Color(0xFF28478A)
private val DesignBg = Color(0xFFF2F0E9)
private val DesignLime = Color(0xFFDCE775)
private val DesignTextBlue = Color(0xFF1A237E)

@Composable
fun GroupsScreen(
    onNavigateToCreateGroup: () -> Unit,
    onNavigateToChat: (String, String) -> Unit,
    onNavigateBack: () -> Unit,           // YENİ: Geri gitme fonksiyonu
    onNavigateToNotifications: () -> Unit // YENİ: Bildirim ekranına gitme fonksiyonu
) {
    val db = FirebaseFirestore.getInstance()
    var allGroups by remember { mutableStateOf<List<GroupData>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("Groups")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val groups = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(GroupData::class.java)
                }
                allGroups = groups
            }
    }

    Scaffold(
        containerColor = DesignBg,
        floatingActionButton = {}
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // --- 1. HEADER ---
                item {
                    HeaderSection(
                        onBackClick = onNavigateBack,
                        onNotificationClick = onNavigateToNotifications
                    )
                }

                // --- 2. ALL GROUPS ---
                item {
                    Text(
                        text = "All Groups",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DesignTextBlue,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (allGroups.isEmpty()) {
                            item {
                                BigGroupCard(
                                    title = "No Groups",
                                    subtitle = "Create one now",
                                    tag = "Info",
                                    backgroundColor = DesignOrange,
                                    onClick = onNavigateToCreateGroup
                                )
                            }
                        } else {
                            items(allGroups) { group ->
                                BigGroupCard(
                                    title = group.name,
                                    subtitle = group.id.take(6).uppercase(),
                                    tag = "Class",
                                    backgroundColor = DesignOrange,
                                    onClick = { onNavigateToChat(group.id, group.name) }
                                )
                            }
                        }
                    }
                }

                // --- 3. MY GROUPS ---
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "My Groups",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DesignTextBlue,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                if (allGroups.isNotEmpty()) {
                    itemsIndexed(allGroups) { index, group ->
                        val cardColor = if (index % 2 == 0) DesignBlue else DesignOrange
                        StackedGroupCard(
                            title = group.name,
                            code = group.id.take(4).uppercase(),
                            description = "Academic Group • ${group.name}",
                            color = cardColor,
                            onClick = { onNavigateToChat(group.id, group.name) }
                        )
                    }
                }
            }

            // --- 4. CREATE BUTTON ---
            Button(
                onClick = onNavigateToCreateGroup,
                colors = ButtonDefaults.buttonColors(containerColor = DesignLime),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .width(160.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "CREATE",
                    color = DesignTextBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// --- BİLEŞENLER ---

@Composable
fun HeaderSection(
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Geri Tuşu
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.05f))
                .clickable { onBackClick() }, // Tıklama işlevi eklendi
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = DesignTextBlue
            )
        }

        // Başlık
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Groups",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DesignTextBlue
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = DesignTextBlue
            )
        }

        // Bildirim İkonu
        // İkonun kendisine tıklama özelliği verdik, ama istenirse etrafına Box koyup ona da verilebilir.
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = DesignTextBlue,
            modifier = Modifier
                .size(28.dp)
                .clickable { onNotificationClick() } // Tıklama işlevi eklendi
        )
    }
}

@Composable
fun BigGroupCard(
    title: String,
    subtitle: String,
    tag: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = DesignBlue.copy(alpha = 0.8f),
                shape = RoundedCornerShape(50),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = tag,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun StackedGroupCard(
    title: String,
    code: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 24.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = color),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = code,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    }
    Spacer(modifier = Modifier.height((-30).dp))
}