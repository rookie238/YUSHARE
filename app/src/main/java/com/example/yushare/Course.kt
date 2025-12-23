package com.example.yushare

import androidx.compose.ui.graphics.Color

// --- RENK PALETİ TANIMLAMALARI ---
// Tasarımda kullanılan renklerin Hex kodları burada merkezi olarak tutulur.
val AppDeepNavy = Color(0xFF23006A)   // Derin lacivert: Başlıklar veya vurgular için
val AppDarkBlue = Color(0xFF294BA3)   // Koyu Mavi: Standart ders kutucukları için
val AppLime = Color(0xFFE2F98C)       // Lime: Dikkat çeken dersler veya butonlar için
val AppOrange = Color(0xFFFE9000)     // Turuncu: Alternatif ders kategorileri için
val AppBg = Color(0xFFEFEDE6)         // Arka plan: Gözü yormayan açık bej tonu
val AppGray = Color(0xFFA3A2A5)       // Gri: Yardımcı metinler veya pasif ikonlar için
val AppWhite = Color(0xFFFFFFFF)      // Saf Beyaz: Kart içleri veya metinler için

// Derslerin hangi renk kategorisine ait olduğunu belirleyen enum sınıfı
enum class CourseColor { BLUE, ORANGE, LIME }

/**
 * Course (Ders) Veri Modeli
 * Uygulama içerisinde bir dersin sahip olduğu tüm özellikleri temsil eder.
 */
data class Course(
    val id: Int,               // Benzersiz ders kimliği
    val code: String,           // Ders kodu (Örn: COMM 101)
    val name: String,           // Dersin tam adı
    val lecturer: String,       // Dersi veren öğretim görevlisi
    val description: String,    // Ders içeriği hakkında kısa bilgi
    val semester: String,       // Dönem bilgisi (Örn: Fall 2025)
    val category: String? = null, // Opsiyonel kategori
    val colorType: CourseColor  // Ders kartının hangi renkte görüneceği
)

// --- ÖRNEK VERİ SETİ
// Arayüzü test etmek ve listeleme yapmak için kullanılan geçici veri listesi.
val sampleCourses = listOf(
    // Fall 2025 (Güz Dönemi) Dersleri
    Course(1, "COMM 101", "Introduction to Communication", "Dr. Ali Yılmaz", "İletişime giriş.", "Fall 2025", null, CourseColor.BLUE),
    Course(2, "COMM 171", "Introduction to Communication Design", "Dr. Ayşe Demir", "Tasarım prensipleri.", "Fall 2025", null, CourseColor.ORANGE),
    Course(3, "VCD 111", "Basic Drawing", "Caner Erkin", "Çizim teknikleri.", "Fall 2025", null, CourseColor.BLUE),
    Course(4, "COMM 199", "Seminar in Academic Writing Skills", "Özlem Akkaya", "Akademik yazım.", "Fall 2025", null, CourseColor.ORANGE),
    Course(5, "TKL 201", "Turkish Language I", "Zeynep Kaya", "Dil yapısı.", "Fall 2025", null, CourseColor.BLUE),
    Course(8, "SOC 101", "Introduction to Sociology", "Dr. Emre Can", "Sosyoloji temelleri.", "Fall 2025", null, CourseColor.ORANGE),

    // Spring 2026 (Bahar Dönemi) Dersleri
    Course(6, "GRA 203", "Typography I", "Mert Yılmaz", "Tipografi.", "Spring 2026", null, CourseColor.LIME),
    Course(7, "VCD 172", "Digital Design", "Burak Şen", "Dijital araçlar.", "Spring 2026", null, CourseColor.BLUE),
    Course(9, "TKL 202", "Turkish Language II", "Zeynep Kaya", "Dil yapısı devam.", "Spring 2026", null, CourseColor.LIME),
    Course(10, "COMM 120", "Art History and Aesthetics", "Selin Işık", "Sanat tarihi.", "Spring 2026", null, CourseColor.BLUE),
    Course(11, "PSY 101", "Introduction to Psychology", "Ahmet Ak", "Psikolojiye giriş.", "Spring 2026", null, CourseColor.LIME)
)