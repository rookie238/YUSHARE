package com.example.yushare

import android.net.Uri // Dosya yolunu (URI) temsil etmek için gerekli
import androidx.activity.compose.rememberLauncherForActivityResult // Dış uygulamadan (galeri vb.) sonuç almak için
import androidx.activity.result.contract.ActivityResultContracts // Standart dosya seçme sözleşmeleri için
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(onBackClick: () -> Unit) {
    // Kullanıcının girdiği açıklama metnini tutan state
    var description by remember { mutableStateOf("") }

    // Seçilen görselin telefon üzerindeki adresini (Uri) tutan state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // launcher: Telefonun dosya seçicisini başlatan ve sonucu yakalayan mekanizma
    // GetContent() sözleşmesi, kullanıcının bir içerik seçip uygulamaya dönmesini sağlar.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri // Kullanıcı bir dosya seçtiğinde Uri buraya atanır
    }

    Scaffold(
        containerColor = AppBg, // Course.kt'de tanımladığın arka plan rengi
        topBar = {
            TopAppBar(
                title = { Text("Upload Content", color = AppDarkBlue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Geri butonu tasarımı
                    IconButton(onClick = onBackClick) {
                        Surface(shape = CircleShape, color = AppLime, modifier = Modifier.size(40.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_back),
                                contentDescription = null,
                                tint = AppDarkBlue,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Ögeler arası sabit boşluk
        ) {
            // --- KURS SEÇİM ALANI ---
            Text("Select Course", fontWeight = FontWeight.Bold, color = AppDarkBlue)
            OutlinedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Courses", color = Color.Gray)
                    Icon(Icons.Default.KeyboardArrowDown, null)
                }
            }

            // --- AÇIKLAMA GİRİŞ ALANI ---
            Text("Description", fontWeight = FontWeight.Bold, color = AppDarkBlue)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Type..") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // --- DOSYA YÜKLEME (EK) ALANI ---
            Text("Attachment (optional)", fontWeight = FontWeight.Bold, color = AppDarkBlue)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(1.dp, AppDarkBlue, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        // Tıklandığında telefonun galerisini/dosyalarını açar
                        // "image/*" parametresi sadece görsellerin seçilmesine izin verir
                        launcher.launch("image/*")
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_upload),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = AppDarkBlue
                    )

                    // Seçim durumuna göre dinamik metin gösterimi
                    Text(
                        text = if (selectedImageUri != null) "File Selected!" else "Click to Upload File",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedImageUri != null) Color(0xFF4CAF50) else AppDarkBlue
                    )

                    // Eğer dosya seçiliyse kullanıcıya değiştirebileceğini hatırlatır
                    if (selectedImageUri != null) {
                        Text(
                            text = "Tap again to change",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // İçeriği yukarı iter, butonu en alta sabitler

            // --- PAYLAŞ BUTONU ---
            Button(
                onClick = {
                    // Paylaşma mantığı (API isteği vb.) buraya eklenecek
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppLime),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SHARE", color = AppDarkBlue, fontWeight = FontWeight.Bold)
            }
        }
    }
}