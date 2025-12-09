package com.example.yushare
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.yushare // Kendi R dosyanızı buraya göre güncelleyin
// Not: Resim kaynakları (R.drawable.image_x) uygulamanızda mevcut olmalıdır.

// Veri Modelleri (Simülasyon Verisi)
data class Course(val code: String)
data class LatestUpload(
    val userHandle: String,
    val courseCode: String,
    val imageResId: Int,
    val likes: Int,
    val caption: String
)

// Örnek Veriler (Tasarımda Görünenleri Taklit Eder)
val popularCourses = listOf(
    Course("VCD 471"),
    Course("VCD 371"),
    Course("VCD 311"),
    Course("VCD 201")
)

val latestUploads = listOf(
    LatestUpload("@merisakooun", "VCD 470", R.drawable.image_1, 7, "@ozgunakman: nasıl buldunuz?"),
    LatestUpload("@nikturgun", "VCD 470", R.drawable.image_2, 10, "@housungurdal: cok sakin yani guzel"),
    LatestUpload("@sibicim", "VCD 470", R.drawable.image_3, 5, "@mehmetcan: anatomiye baslama?"),
    LatestUpload("@emircan", "VCD 273", R.drawable.image_4, 8, "@emircan: harikasin paylasim"),
)
// Not: Yukarıdaki R.drawable.image_x'ler sizin projenizde mevcut resimlerinizle eşleşmelidir.

//----------------------------------------------------------------------
// 1. Ana Ekran Composable'ı
//----------------------------------------------------------------------

@Composable
fun CoursesScreen() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar() },
        containerColor = Color(0xFFF0F8FF) // Açık mavi arka plan rengi
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            // Popüler Dersler Bölümü
            item {
                Text(
                    "Popular Courses",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                PopularCoursesRow(popularCourses)
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray)
            }

            // Grup & Yardım (Group & Help) Bölümü
            item {
                Text(
                    "Group & Help",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                GroupHelpRow(popularCourses.take(2)) // Örnek olarak ilk 2 dersi kullandık
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray)
            }

            // Son Yüklemeler Bölümü
            item {
                Text(
                    "Latest Upload",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Son Yükleme Öğeleri
            items(latestUploads) { upload ->
                LatestUploadItem(upload)
            }
        }
    }
}

//----------------------------------------------------------------------
// 2. Özel Bileşenler
//----------------------------------------------------------------------

// Popüler Kurslar Yatay Listesi
@Composable
fun PopularCoursesRow(courses: List<Course>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses.take(2)) { course ->
            CourseCard(course.code, isLarge = true) // Büyük Kartlar
        }
        items(courses.drop(2)) { course ->
            CourseCard(course.code, isLarge = false) // Küçük Kartlar
        }
    }
}

// Grup & Yardım Yatay Listesi
@Composable
fun GroupHelpRow(courses: List<Course>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CourseCard("Maya", isLarge = false, backgroundColor = Color(0xFFE0E0E0)) // Gri Ton
        }
        items(courses) { course ->
            CourseCard(course.code, isLarge = false)
        }
    }
}

// Ders Kartı Bileşeni
@Composable
fun CourseCard(title: String, isLarge: Boolean, backgroundColor: Color = Color(0xFFE0E0E0)) {
    Card(
        modifier = Modifier
            .width(if (isLarge) 160.dp else 100.dp)
            .height(if (isLarge) 100.dp else 50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = if (isLarge) 18.sp else 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF555555)
            )
        }
    }
}

// Son Yükleme Öğesi (Card)
@Composable
fun LatestUploadItem(upload: LatestUpload) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Başlık (Handle ve Ders Kodu)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = upload.userHandle,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B5998)
                )
                Text(
                    text = upload.courseCode,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Resim
            Image(
                painter = painterResource(id = upload.imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Beğeni Sayısı
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = Color.Red,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = upload.likes.toString(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Açıklama/Yorum
            Text(
                text = upload.caption,
                fontSize = 14.sp
            )
        }
    }
}

//----------------------------------------------------------------------
// 3. Sabit Bileşenler (TopBar ve BottomNavBar)
//----------------------------------------------------------------------

// Üst Çubuk (Top Bar)
@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kullanıcı Profili (Avatar)
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Başlık
        Text(
            text = "Yeditepe Not Paylaşım",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        // Bildirim Zili
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            modifier = Modifier.size(24.dp)
        )
    }
}

// Alt Navigasyon Çubuğu (Bottom Navigation Bar)
@Composable
fun BottomNavBar() {
    BottomAppBar(
        containerColor = Color(0xFF673AB7) // Mor renk
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigasyon Öğeleri (Tasarımı taklit ederek 4 adet)
            NavBarItem(Icons.Default.Home, "Home", Color.White)
            NavBarItem(Icons.Default.Edit, "Upload", Color.White.copy(alpha = 0.6f))
            NavBarItem(Icons.Default.AddCircle, "Add", Color.Red, isLarge = true) // + butonu
            NavBarItem(Icons.Default.Search, "Search", Color.White.copy(alpha = 0.6f))
            NavBarItem(Icons.Default.Person, "Profile", Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun RowScope.NavBarItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, tint: Color, isLarge: Boolean = false) {
    if (isLarge) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(50.dp) // Büyük + butonu
        )
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint
            )
        }
    }
}

//----------------------------------------------------------------------
// 4. Önizleme Fonksiyonu
//----------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun PreviewCoursesScreen() {
    MaterialTheme {
        CoursesScreen()
    }
}
