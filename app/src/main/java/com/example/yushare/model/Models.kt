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
    val username: String = "",
    val courseCode: String = "", // <-- BURASI String OLMALI
    val fileUrl: String = "",
    val description: String = "",
    val fileType: String = "text"
)

data class UserProfile(
    val name: String = "Yükleniyor...",
    val studentId: String = "",
    val department: String = "",
    val degree: String = "",
    val bio: String = ""         // Örn: "Kod yazmayı ve kahve içmeyi severim."
)