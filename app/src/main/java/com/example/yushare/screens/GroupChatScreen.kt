package com.example.yushare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.yushare.R
import com.example.yushare.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.example.yushare.viewmodel.ChatViewModel
import kotlin.collections.isNotEmpty

// Mesaj Modeli
data class ChatMessage(
    val id: Long,
    val text: String,
    val senderName: String,
    val avatarRes: Int?,
    val isFromMe: Boolean = false
)

@Composable
fun ChatScreen(
    navController: NavController,
    groupName: String,
    viewModel: ChatViewModel // ViewModel parametresi
) {
    // --- STATE TANIMLARI ---

    // ADIM 2 UYGULANDI: Mesajları ViewModel'den çekiyoruz (Grup ismine göre)
    // remember(groupName) kullanıyoruz ki grup değişirse liste güncellensin.
    val messages = remember(groupName) { viewModel.getMessages(groupName) }

    // Input Alanındaki Metin
    var inputText by remember { mutableStateOf("") }

    // Scroll Kontrolü
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Mesaj Gönderme Fonksiyonu
    fun sendMessage() {
        if (inputText.isNotBlank()) {
            // ADIM 2 UYGULANDI: Mesajı ViewModel üzerinden gönderiyoruz
            viewModel.sendMessage(groupName, inputText)

            inputText = "" // Kutuyu temizle

            // Listeyi en aşağı kaydır
            coroutineScope.launch {
                // UI'ın listeyi güncellemesi milisaniyeler alabilir, bu yüzden
                // mesaj eklendikten sonra listenin en sonuna gidiyoruz.
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.ime,
        containerColor = YuBackground,
        bottomBar = {
            ChatBottomInputArea(
                text = inputText,
                onTextChanged = { inputText = it },
                onSendClicked = { sendMessage() }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Header (Grup İsmi)
            ChatHeader(
                groupName = groupName,
                onBackClick = { navController.popBackStack() }
            )

            // 2. Mesaj Listesi
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 20.dp)
            ) {
                items(messages) { message ->
                    MessageItem(message)
                }
            }
        }
    }
}

// --- BİLEŞENLER ---

@Composable
fun ChatHeader(groupName: String, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Geri Butonu ve Başlık Row'u
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            // Geri Butonu
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDBDEE7))
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    tint = YuMediumBlue
                )
            }

            // Groups Başlığı
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Groups",
                    color = YuMediumBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = YuMediumBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Turuncu Grup İsmi Kartı
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(YuOrange),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = groupName,
                color = YuMediumBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    // Mesaj benden ise SAĞA, başkasından ise SOLA hizala
    val arrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.Bottom
    ) {
        // Eğer mesaj başkasındansa Avatarı göster
        if (!message.isFromMe && message.avatarRes != null) {
            Image(
                painter = painterResource(id = message.avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        // Konuşma Balonu
        Column(horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start) {

            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .background(
                        color = if (message.isFromMe) YuMediumBlue else YuChatBubble,
                        shape = if (message.isFromMe)
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 0.dp)
                        else
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 0.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (message.isFromMe) Color.White else Color.Black.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ChatBottomInputArea(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(YuBackground)
            .imePadding()
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(YuInputBg)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol taraftaki Artı Butonu
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(YuMediumBlue)
                        .clickable { /* Medya ekleme aksiyonu */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Input Alanı
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Type Message",
                            color = YuMediumBlue.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }

                    BasicTextField(
                        value = text,
                        onValueChange = onTextChanged,
                        textStyle = TextStyle(
                            color = YuMediumBlue,
                            fontSize = 14.sp
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(YuMediumBlue),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = { onSendClicked() }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Gönder Butonu
                if (text.isNotBlank()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = YuMediumBlue,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onSendClicked() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupChatScreenPreview() {
    val navController = rememberNavController()
    // Preview için geçici bir ViewModel oluşturuyoruz
    val mockViewModel = ChatViewModel()
    ChatScreen(
        navController = navController,
        groupName = "UI Design Study Group",
        viewModel = mockViewModel
    )
}