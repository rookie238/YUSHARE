package com.example.yushare

data class Course(
    val id: Int,
    val code: String,       // Örn: COMM 101
    val name: String,       // Örn: Introduction to Communication
    val lecturer: String,   // Örn: Özlem Akkaya
    val description: String // Dersin açıklaması
)

// ders listesi
val sampleCourses = listOf(
    Course(1, "COMM 101", "Introduction to Communication", "Dr. Ali Yılmaz", "İletişime giriş dersi."),
    Course(2, "COMM 171", "Introduction to Communication Design", "Dr. Ayşe Demir", "Temel iletişim tasarımı prensipleri."),
    Course(3, "COMM 199", "Seminar in Academic Writing Skills", "Özlem Akkaya", "This course is an introduction to academic writing skills."),
    Course(4, "SOC 101", "Introduction to Sociology", "Prof. Mehmet Demir", "Toplum bilimine giriş."),
    Course(5, "TKL 201", "Turkish Language I", "Okt. Zeynep Kaya", "Türk dilinin yapısı ve tarihi."),
    Course(6, "VCD 111", "Basic Drawing", "Öğr. Gör. Caner Erkin", "Temel çizim teknikleri ve perspektif."),
    Course(7, "ART 101", "Art History and Aesthetic", "Dr. Selin Vural", "Sanat tarihi ve estetik algısı."),
    Course(8, "GRA 203", "Typography", "Öğr. Gör. Mert Yılmaz", "Tipografinin temelleri ve yazı karakterleri."),
    Course(9, "TKL 202", "Turkish Language II", "Okt. Zeynep Kaya", "İleri seviye Türkçe dil bilgisi."),
    Course(10, "VCD 172", "Digital Design", "Dr. Burak Şen", "Dijital tasarım araçlarına giriş."),
    Course(11, "PSY 101", "Introduction to Psychology", "Prof. Banu Çelik", "İnsan davranışları ve zihinsel süreçler.")
)