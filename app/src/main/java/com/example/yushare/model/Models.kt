package com.example.yushare.model

data class Course(
    val id: String = "",
    val title: String = "",       // Örn: COMM 101
    val subtitle: String = "",    // Örn: Introduction to Communication
    val term: String = "",        // Örn: Fall 2025 (YENİ)
    val lecturer: String = "",    // Örn: Özlem Akkaya (YENİ)
    val description: String = ""  // Ders açıklaması (YENİ)
)

// BURASI ÖNEMLİ:
// SharedViewModel içinde "courseCode" string olarak kullanıldığı için
// burada da String olmalı. (Course nesnesi OLMAMALI)
data class Post(
    val id: String = "",
    val ownerId: String = "",
    val username: String = "",
    val courseCode: String = "", // <-- BURASI String OLMALI
    val fileUrl: String = "",
    val description: String = "",
    val fileType: String = "text",
    var isLiked: Boolean = false
)

data class UserProfile(
    val name: String = "Yükleniyor...",
    val studentId: String = "",
    val department: String = "",
    val degree: String = "",
    val bio: String = ""         // Örn: "Kod yazmayı ve kahve içmeyi severim."
)

data class NotificationItem(
    val id: String = "",
    val toUserId: String = "",      // Bildirim kime gidecek?
    val fromUserName: String = "",  // Bildirimi kim oluşturdu? (Örn: ardademir)
    val fromUserAvatar: String = "",// Yapan kişinin resmi (varsa)
    val actionType: String = "",    // "LIKE" veya "MESSAGE"
    val message: String = "",       // "liked your post"
    val timestamp: Long = 0,
    val isRead: Boolean = false
)

// ... Diğer sınıfların altına ekle ...

data class Comment(
    val id: String = "",
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis() // Sıralama için zaman damgası
)