package com.example.yushare.model

data class Course(
    val id: String = "",
    val title: String = "",
    val subtitle: String = ""
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