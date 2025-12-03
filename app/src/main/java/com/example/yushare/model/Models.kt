package com.example.yushare.model

data class Course(
    val id: String = "",
    val title: String = "",
    val subtitle: String = ""
)

data class Post(
    val id: String = "",
    val username: String = "",
    val courseCode: String = "",
    val fileUrl: String = "",    // Artık sadece resim değil, herhangi bir dosya olabilir
    val description: String = "", // Yorum alanı
    val fileType: String = "text" // "image", "pdf", "audio", "text" olabilir
)