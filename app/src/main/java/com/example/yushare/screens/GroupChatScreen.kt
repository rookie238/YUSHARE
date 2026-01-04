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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Bu import'un ekli olduğundan emin ol (build.gradle'da coil varsa çalışır)
import com.example.yushare.R // Projenin R dosyasını import ettiğinden emin ol
import com.example.yushare.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.Send

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
    onNavigateToNotifications: () -> Unit,
    groupId: String,
    groupName: String
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""

    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    val listState = rememberLazyListState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
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
                onNotificationClick = onNavigateToNotifications
            )
        },
        bottomBar = {
            ChatInputArea(
                text = messageText,
                onTextChanged = { messageText = it },
                onSend = { sendMessage() },
                onAttachClick = { launcher.launch("*/*") }
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
                    .clickable { onBackClick() },
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
                    .clickable { onNotificationClick() },
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
            .height(100.dp) // Yükseklik
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(ChatInputBg)
            .padding(horizontal = 16.dp, vertical = 20.dp), // Kenar boşlukları ayarlandı
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- 1. SOLDAKİ (+) BUTONU ---
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ChatDarkBlue)
                    .clickable { onAttachClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(10.dp))

            // --- 2. ORTADAKİ YAZI ALANI ---
            Box(
                modifier = Modifier
                    .weight(1f) // Kalan boşluğu doldur
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

            Spacer(modifier = Modifier.width(10.dp))

            // --- 3. SAĞDAKİ GÖNDER (SEND) BUTONU ---
            // Eğer yazı varsa ChatDarkBlue (Mavi), yoksa Gri renk olur.
            val isButtonEnabled = text.isNotBlank()
            val buttonColor = if (isButtonEnabled) ChatDarkBlue else Color.Gray

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .clickable(enabled = isButtonEnabled) { onSend() },
                contentAlignment = Alignment.Center
            ) {
                // Not: Eğer 'Icons.AutoMirrored.Filled.Send' hata verirse 'Icons.Default.Send' kullan.
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp) // İkon boyutu
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
        // Arka Plan Halkaları
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(color = CircleLight, radius = size.width * 0.7f, center = center)
            drawCircle(color = CircleLight.copy(alpha = 0.5f), radius = size.width * 0.5f, center = center)
            drawCircle(color = CircleLight.copy(alpha = 0.7f), radius = size.width * 0.3f, center = center)
        }

        // --- FOTOĞRAFLI AVATARLAR ---
        // Not: Bu resimlerin (resim11, resim12 vb.) drawable klasöründe olduğundan emin ol.

        // A Kişisi (Üst Orta) - resim15 (Asyalı kadın)
        ImageAvatar(
            modifier = Modifier.align(Alignment.TopCenter).offset(y = 80.dp),
            imageRes = R.drawable.resim15
        )

        // B Kişisi (Sol Orta) - resim11 (Turunculu erkek)
        ImageAvatar(
            modifier = Modifier.align(Alignment.CenterStart).offset(x = 60.dp, y = (-40).dp),
            imageRes = R.drawable.resim11
        )

        // C Kişisi (Sağ Orta) - resim14 (Kahverengi saçlı kadın)
        ImageAvatar(
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-60).dp, y = (-80).dp),
            imageRes = R.drawable.resim14
        )

        // D Kişisi (Alt Sol) - resim12 (Sarışın kadın)
        ImageAvatar(
            modifier = Modifier.align(Alignment.BottomStart).offset(x = 80.dp, y = (-150).dp),
            imageRes = R.drawable.resim12
        )

        // E Kişisi (Alt Sağ) - resim13 (Mavili erkek)
        ImageAvatar(
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-80).dp, y = (-120).dp),
            imageRes = R.drawable.resim13
        )

        // Ortadaki "Ben" - Kullanıcının kendi resmi (Örnek olarak resim11'i kullandım, değiştirebilirsin)
        ImageAvatar(
            modifier = Modifier.align(Alignment.Center),
            imageRes = R.drawable.resim11, // Kendi profil resmin buraya gelebilir
            size = 60.dp
        )

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

// Yeni: Fotoğraf Gösteren Avatar Bileşeni
@Composable
fun ImageAvatar(
    modifier: Modifier = Modifier,
    imageRes: Int, // R.drawable.xxx alacak
    size: androidx.compose.ui.unit.Dp = 50.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color.White, CircleShape)
            .shadow(4.dp, CircleShape), // Hafif bir gölge eklendi
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageRes,
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop, // Resmi daireye sığdır
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
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