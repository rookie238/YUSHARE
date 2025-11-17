package com.example.yushare.ui.profile
// ProfileScreen.kt dosyanızın içi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import com.example.yushare.R
import com.example.yushare.ui.theme.TextGray
import com.example.yushare.ui.theme.ProfileCardBackground
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import com.example.yushare.ui.theme.AppPurple
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.heightIn
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController



@OptIn(ExperimentalMaterial3Api::class) // TopAppBar için bu gerekli
@Composable
fun ProfileTopBar() {
    TopAppBar(
        // Başlık (Title)
        title = {
            Text(
                text = "PROFILE",
                color = AppPurple, // Figma'dan aldığımız mor renk
            )
        },

        // Sağdaki Hamburger Menü İkonu
        actions = {
            IconButton(onClick = { /* Tıklanınca ne olacağını buraya yazacağız */ }) {
                Icon(
                    imageVector = Icons.Default.Menu, // Hazır Material ikonu
                    contentDescription = "Menü",
                    tint = Color.Gray // Tasarımdaki griye benziyor, TextGray de olabilir
                )
            }
        },

        // Arka plan rengi. Tasarımda beyaz görünüyor.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}



@Preview(showBackground = true)
@Composable
fun ProfileTopBarPreview() {
    ProfileTopBar()
}
@Composable
fun ProfileInfoCard() {
    // Box: Bileşenleri üst üste yığmak (stack) için kullanırız.
    // Card'ı sildik çünkü Card, arka plan resmi için uygun değil.
    Box(
        // Kutunun içindeki her şeyi (avatar, yazılar)
        // yukarıda ve ortada hizala
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(horizontal = 16.dp) // Ekranın kenarlarından boşluk
            .fillMaxWidth() // Tüm genişliği kapla
    ) {

        // 1. KATMAN (EN ALT): Dalgalı Arka Plan Resmimiz
        Image(
            // res/drawable'a attığımız yeni resim
            painter = painterResource(id = R.drawable.profile_card_background),
            contentDescription = "Profil Arka Planı",
            // Resmin, kutunun genişliğine sığmasını sağla
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        // 2. KATMAN (ÜSTTE): Bilgilerin olduğu dikey sütun
        // Bu Column, arka plan resminin "üstünde" görünecek
        Column(
            // Sütunun içindeki her şeyi yatayda ortala
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                // Tasarıma benzemesi için üstten biraz boşluk verelim
                .padding(top = 24.dp)
        ) {

            // Profil Fotoğrafı (Burası aynı)
            Image(
                painter = painterResource(id = R.drawable.profile_avatar),
                contentDescription = "Profil Fotoğrafı",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // İsim (Burası aynı)
            Text(
                text = "Arda Demir",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bölüm (Burası aynı)
            Text(
                text = "Department: Visual Communication Design",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Öğrenci ID (Burası aynı)
            Text(
                text = "Student Id: 20210584978",
                fontSize = 14.sp,
                color = TextGray
            )
        }
    }
}


// ÖNİZLEME FONKSİYONU (Hata vermemesi için bunu da güncelle)
@Preview(showBackground = true)
@Composable
fun ProfileInfoCardPreview() {
    // ProjenizinAnaTemasi { ... }
    ProfileInfoCard()
}

// YENİ FONKSİYON: NOTLAR KARTI
@Composable
fun NotesCard() {
    Card(
        // Yine 40.dp oval kenar verelim
        shape = RoundedCornerShape(40.dp),
        // Arka plan rengi Profil Kartı ile aynı
        colors = CardDefaults.cardColors(
            containerColor = ProfileCardBackground
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp) // Yanlardan 16dp boşluk
            .fillMaxWidth() // Tam genişlik
            .heightIn(min = 100.dp) // Minimum 100dp yüksekliği olsun
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp) // İç boşluklar
        ) {
            Text(
                text = "Notes:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            // Notlar kısmı için şimdilik boş bir alan
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Buraya notlar gelecek...",
                fontSize = 14.sp,
                color = TextGray
            )
        }
    }
}

// Notlar kartı için de bir önizleme (isteğe bağlı)
@Preview(showBackground = true)
@Composable
fun NotesCardPreview() {
    NotesCard()
}

// YENİ FONKSİYON 1: ALT NAVİGASYON MENÜSÜ
@Composable
fun ProfileBottomNav() {
    BottomAppBar(
        containerColor = Color.White, // Arka planı beyaz
        tonalElevation = 8.dp // Hafif bir gölge verir
    ) {
        // Row: İkonları yatayda dizmek için
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // İkonları tek tek ekleyelim
            // Tasarımda "Profile" ikonu seçili (mor), diğerleri değil (gri).

            BottomNavItemVector(icon = Icons.Default.Home, description = "Ana Sayfa", isSelected = false)
            BottomNavItemDrawable(
                drawableId = R.drawable.ic_edit_custom, // res/drawable/ic_edit_custom.png
                description = "Düzenle",
                isSelected = false // Seçili değil, bu yüzden gri görünecek
            )
            BottomNavItemVector(icon = Icons.Default.Add, description = "Ekle", isSelected = false) // Add ikonu

            // "Groups" için standart bir ikon seçtim, Figma'dan doğrusunu alabiliriz
            BottomNavItemVector(icon = Icons.Default.Search, description = "Gruplar", isSelected = false)

            BottomNavItemVector(icon = Icons.Default.Person, description = "Profil", isSelected = true) // Profil seçili
        }
    }
}

// YENİ FONKSİYON 2: ALT MENÜDEKİ TEK BİR İKON İÇİN
@Composable
fun BottomNavItemVector(icon: ImageVector, description: String, isSelected: Boolean) {
    IconButton(onClick = { /* Tıklanınca bir şey yapacak */ }) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            // Eğer seçili (isSelected) ise AppPurple, değilse gri yap
            tint = if (isSelected) AppPurple else Color.Gray,
            modifier = Modifier.size(32.dp) // İkonları biraz büyütelim
        )
    }
}
// YENİ FONKSİYON: PNG/Drawable ikonlar için
@Composable
fun BottomNavItemDrawable(drawableId: Int, description: String, isSelected: Boolean, ) {
    IconButton(onClick = { /* Tıklanınca bir şey yapacak */ }) {
        Icon(
            // painterResource ile drawable klasöründen resmi yüklüyoruz
            painter = painterResource(id = drawableId),
            contentDescription = description,

            // TINT AYARI:
            // Eğer ikon seçiliyse (isSelected = true), kendi renginde kalsın (Unspecified).
            // 'tint' (renk ayarı) HER ZAMAN 'Unspecified' olacak.
            tint = Color.Unspecified,


            modifier = Modifier.size(32.dp)
        )
    }
}


// YENİ FONKSİYON 3: TÜM EKRANI BİRLEŞTİRME
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            ProfileTopBar()
        },
        bottomBar = {
            ProfileBottomNav()
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding) // Scaffold'un padding'ini uygula
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Her şeyi yatayda ortala
        ) {
            // 1. Kart: Profil Bilgileri
            ProfileInfoCard()

            // İki kart arasına boşluk
            Spacer(modifier = Modifier.height(16.dp))

            // 2. Kart: Notlar
            NotesCard()
        }
    }
}



// YENİ ÖNİZLEME: Tüm ekranı görmek için
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    // ProjenizinAdiTheme {

    // Önizlemenin çalışması için sahte bir NavController oluştur:
    val fakeNavController = rememberNavController()

    // Onu buraya parametre olarak ver:
    ProfileScreen(navController = fakeNavController) // <-- ARTIK HATA VERMEZ

    // }
}