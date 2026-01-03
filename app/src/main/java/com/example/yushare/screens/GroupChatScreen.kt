package com.example.yushare.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

// --- Renkler ---
private val ChatBg = Color(0xFFF2F0E9)
private val ChatOrange = Color(0xFFFFCC80)
private val ChatDarkBlue = Color(0xFF28478A)
private val ChatInputBg = Color(0xFFD8DEE9)
private val CircleLight = Color(0xFFB0BEC5).copy(alpha = 0.3f)

data class ChatMessage(
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun GroupChatScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNotifications: () -> Unit, // YENİ: Bildirim parametresi
    groupId: String,
    groupName: String
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""

    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    val listState = rememberLazyListState()

    // --- DOSYA SEÇİCİ (Dosya Ekleme Tuşu İçin) ---
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // Dosya seçildiğinde burası çalışır.
        // Şimdilik sadece seçilen URI'yi konsola yazdırıyoruz.
        // İleride buraya Firebase Storage yükleme kodu eklenebilir.
        println("Seçilen Dosya: $uri")
    }

    LaunchedEffect(groupId) {
        db.collection("Groups").document(groupId).collection("Messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener
                val newMessages = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ChatMessage::class.java)
                }
                messages = newMessages
            }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage() {
        if (messageText.isBlank()) return
        val newMessageRef = db.collection("Groups").document(groupId).collection("Messages").document()
        val newMessage = ChatMessage(
            id = newMessageRef.id,
            text = messageText,
            senderId = currentUserId,
            senderName = auth.currentUser?.displayName ?: "User",
            timestamp = System.currentTimeMillis()
        )
        newMessageRef.set(newMessage)
        messageText = ""
    }

    Scaffold(
        containerColor = ChatBg,
        topBar = {
            ChatHeader(
                groupName = groupName,
                onBackClick = onNavigateBack,
                onNotificationClick = onNavigateToNotifications // Parametre bağlandı
            )
        },
        bottomBar = {
            ChatInputArea(
                text = messageText,
                onTextChanged = { messageText = it },
                onSend = { sendMessage() },
                onAttachClick = { launcher.launch("*/*") } // Tüm dosya türlerini açar
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (messages.isEmpty()) {
                EmptyChatDesign()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        val isMe = msg.senderId == currentUserId
                        MessageBubble(message = msg, isMe = isMe)
                    }
                }
            }
        }
    }
}

// --- BİLEŞENLER ---

@Composable
fun ChatHeader(
    groupName: String,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ChatBg)
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Geri Butonu
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.05f))
                    .clickable { onBackClick() }, // Tıklama aktif
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ChatDarkBlue)
            }

            // Başlık
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Groups",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChatDarkBlue
                )
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = ChatDarkBlue)
            }

            // Bildirim Butonu
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onNotificationClick() }, // Tıklama aktif
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notif", tint = ChatDarkBlue)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ChatOrange.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = groupName,
                color = ChatDarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ChatInputArea(
    text: String,
    onTextChanged: (String) -> Unit,
    onSend: () -> Unit,
    onAttachClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(ChatInputBg)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Artı (+) Butonu
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ChatDarkBlue)
                    .clickable { onAttachClick() }, // Dosya seçici tetiklenir
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(21.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty()) {
                    Text("Type Message", color = Color.Gray, fontSize = 14.sp)
                }
                BasicTextField(
                    value = text,
                    onValueChange = onTextChanged,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { onSend() }),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun EmptyChatDesign() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(color = CircleLight, radius = size.width * 0.7f, center = center)
            drawCircle(color = CircleLight.copy(alpha = 0.5f), radius = size.width * 0.5f, center = center)
            drawCircle(color = CircleLight.copy(alpha = 0.7f), radius = size.width * 0.3f, center = center)
        }

        AvatarPlaceHolder(Modifier.align(Alignment.TopCenter).offset(y = 80.dp), "A", Color(0xFFEF5350))
        AvatarPlaceHolder(Modifier.align(Alignment.CenterStart).offset(x = 60.dp, y = (-40).dp), "B", Color(0xFF42A5F5))
        AvatarPlaceHolder(Modifier.align(Alignment.CenterEnd).offset(x = (-60).dp, y = (-80).dp), "C", Color(0xFF66BB6A))
        AvatarPlaceHolder(Modifier.align(Alignment.BottomStart).offset(x = 80.dp, y = (-150).dp), "D", Color(0xFFFFA726))
        AvatarPlaceHolder(Modifier.align(Alignment.BottomEnd).offset(x = (-80).dp, y = (-120).dp), "E", Color(0xFFAB47BC))

        AvatarPlaceHolder(Modifier.align(Alignment.Center), "Me", ChatDarkBlue, size = 60.dp)

        Text(
            text = "Write your first message",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

@Composable
fun AvatarPlaceHolder(modifier: Modifier, text: String, color: Color, size: androidx.compose.ui.unit.Dp = 50.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, isMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(message.senderName.take(1).uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
            if (!isMe) {
                Text(message.senderName, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp, bottom = 2.dp))
            }
            Box(
                modifier = Modifier
                    .widthIn(max = 250.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = if (isMe) 20.dp else 4.dp, bottomEnd = if (isMe) 4.dp else 20.dp))
                    .background(if (isMe) ChatDarkBlue else ChatOrange)
                    .padding(16.dp)
            ) {
                Text(message.text, color = if (isMe) Color.White else ChatDarkBlue, fontSize = 14.sp)
            }
            Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp), fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}