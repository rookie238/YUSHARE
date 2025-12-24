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
    val fileUrl: String = "",
    val description: String = "",
    val fileType: String = "text"
)