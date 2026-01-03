package com.example.yushare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R
import com.example.yushare.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

// Firestore'daki yapına uygun Kullanıcı Modeli
data class GroupUser(
    val uid: String = "",
    val name: String = "",
    val id: String = "",       // Öğrenci ID'si (Firestore'daki 'id' alanı)
    val department: String = "" // Bölüm bilgisi
)

@Composable
fun CreateGroupScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMessaging: () -> Unit
) {
    // --- STATE TANIMLARI ---
    var groupName by remember { mutableStateOf("") }
    var studentIdQuery by remember { mutableStateOf("") }

    // Firestore veritabanı örneği
    val db = FirebaseFirestore.getInstance()

    // Arama sonuçlarını ve seçilenleri tutacak listeler
    var searchResults by remember { mutableStateOf<List<GroupUser>>(emptyList()) }
    val selectedMembers = remember { mutableStateListOf<GroupUser>() }
    var isLoading by remember { mutableStateOf(false) }

    // --- FIRESTORE ARAMA MANTIĞI ---
    // studentIdQuery her değiştiğinde bu blok çalışır
    LaunchedEffect(studentIdQuery) {
        if (studentIdQuery.length >= 3) { // En az 3 karakter yazılınca aramaya başla
            isLoading = true

            // Firestore'da 'id' alanı yazılan değerle başlayanları bul (Prefix Search)
            // Örn: '202' yazınca '2020...', '2021...' hepsi gelir.
            db.collection("Users")
                .whereGreaterThanOrEqualTo("id", studentIdQuery)
                .whereLessThanOrEqualTo("id", studentIdQuery + "\uf8ff")
                .get()
                .addOnSuccessListener { documents ->
                    val users = documents.mapNotNull { doc ->
                        // Firestore verisini GroupUser nesnesine çeviriyoruz
                        val data = doc.data
                        GroupUser(
                            uid = doc.id,
                            name = data["name"] as? String ?: "",
                            id = data["id"] as? String ?: "",
                            department = data["department"] as? String ?: ""
                        )
                    }.filter { user ->
                        // Zaten seçili olanları arama sonucunda gösterme
                        selectedMembers.none { it.id == user.id }
                    }
                    searchResults = users
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                    println("Hata: ${it.message}")
                }
        } else {
            searchResults = emptyList()
            isLoading = false
        }
    }

    Scaffold(
        containerColor = YuBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Üst Bar
            CreateGroupHeader(onBackClick = onNavigateBack)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Grup İsmi Girişi
            GroupNameInputSection(
                groupName = groupName,
                onNameChange = { groupName = it },
                onNextClick = onNavigateToMessaging
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Alt Alan (Üye Seçimi ve Arama)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color(0xFFD9DCE3))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Başlık ve Seçilen Sayısı
                    Text(
                        text = if (groupName.isEmpty()) "New Group" else groupName,
                        color = YuMediumBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Selected members: ${selectedMembers.size}",
                        color = YuMediumBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    // --- SEÇİLEN ÜYELER (GRID) ---
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                    ) {
                        items(selectedMembers) { user ->
                            MemberAvatarItem(user) {
                                // Tıklayınca listeden çıkarma
                                selectedMembers.remove(user)
                            }
                        }
                        // Boş placeholderlar (Dolu görünmesi için opsiyonel)
                        if (selectedMembers.isEmpty()) {
                            items(3) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.3f))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("or", color = YuMediumBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- ID ARAMA INPUTU ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF9FA8DA).copy(alpha = 0.4f))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (studentIdQuery.isEmpty()) {
                            Text(
                                text = "Write student ID to search...",
                                color = YuMediumBlue.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = studentIdQuery,
                            onValueChange = { studentIdQuery = it },
                            textStyle = TextStyle(
                                color = YuMediumBlue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // --- ARAMA SONUÇLARI LİSTESİ ---
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp).size(24.dp), color = YuMediumBlue)
                    } else if (studentIdQuery.length >= 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        ) {
                            if (searchResults.isEmpty()) {
                                item {
                                    Text(
                                        text = "No user found with this ID",
                                        modifier = Modifier.padding(16.dp),
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                items(searchResults) { user ->
                                    SearchResultItem(user = user) {
                                        // Kullanıcıyı ekle ve aramayı temizle
                                        selectedMembers.add(user)
                                        studentIdQuery = ""
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// YARDIMCI BİLEŞENLER
// ==========================================

@Composable
fun SearchResultItem(user: GroupUser, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profil resmi (Statik placeholder)
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = user.name, fontWeight = FontWeight.Bold, color = YuMediumBlue, fontSize = 14.sp)
            Text(
                text = "${user.id} • ${user.department}",
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Add, contentDescription = "Add", tint = YuMediumBlue)
    }
}

@Composable
fun MemberAvatarItem(user: GroupUser, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = user.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        // İsim çok uzunsa sadece ilk kelimeyi al
        Text(
            text = user.name.split(" ").firstOrNull() ?: user.name,
            fontSize = 12.sp,
            color = YuMediumBlue,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CreateGroupHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFDBDEE7))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = YuMediumBlue)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Groups", color = YuMediumBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = YuMediumBlue)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.Notifications, contentDescription = "Notif", tint = YuMediumBlue, modifier = Modifier.size(28.dp))
    }
}

@Composable
fun GroupNameInputSection(groupName: String, onNameChange: (String) -> Unit, onNextClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFD9DCE3))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (groupName.isEmpty()) Text("Group name", color = YuMediumBlue.copy(0.4f), fontSize = 16.sp)
            BasicTextField(
                value = groupName,
                onValueChange = onNameChange,
                textStyle = TextStyle(color = YuMediumBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text("Next", color = YuMediumBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNextClick() })
    }
}