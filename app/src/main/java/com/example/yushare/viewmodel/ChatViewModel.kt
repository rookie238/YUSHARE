package com.example.yushare.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.yushare.R
import com.example.yushare.screens.ChatMessage


class ChatViewModel : ViewModel() {
    // String (Grup İsmi) -> List<ChatMessage> (Mesaj Listesi) eşleşmesi
    private val _chatHistory = mutableMapOf<String, MutableList<ChatMessage>>()

    fun getMessages(groupName: String): List<ChatMessage> {
        // Eğer bu grup için henüz bir liste yoksa, oluştur ve varsayılan mesaj ekle
        if (!_chatHistory.containsKey(groupName)) {
            _chatHistory[groupName] = mutableStateListOf(
                ChatMessage(
                    1,
                    "Merhaba! Burası $groupName grubu.",
                    "Sistem",
                    R.drawable.ic_nav_groups,
                    false
                )
            )
        }
        return _chatHistory[groupName]!!
    }

    fun sendMessage(groupName: String, text: String) {
        val messages = _chatHistory[groupName]
        messages?.add(
            ChatMessage(
                id = System.currentTimeMillis(),
                text = text,
                senderName = "Me",
                avatarRes = null,
                isFromMe = true
            )
        )
    }
}