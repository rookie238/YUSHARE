package com.example.yushare.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.yushare.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(viewModel: SharedViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val isUploading = viewModel.isUploading.value

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var mimeType by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedUri = uri
            mimeType = context.contentResolver.getType(uri) ?: ""
        }
    }

    val courseList = viewModel.coursesList
    var expanded by remember { mutableStateOf(false) }
    var selectedCourseTitle by remember { mutableStateOf("") }

    // --- GÖRSELDEKİ RENK PALETİ ---
    val creamBg = Color(0xFFF2F1EC)       // Görseldeki kirli beyaz/krem arka plan
    val limeGreen = Color(0xFFD9F270)     // Canlı limon yeşili (Buton ve Geri tuşu)
    val darkPurple = Color(0xFF2E1A5B)    // Yazılar ve ikonlar için koyu mor/lacivert
    val inputFill = Color(0xFFEBEBEB)     // Input alanlarının gri arka planı
    val purpleBorder = Color(0xFF6A71C5)  // Çerçeveler ve ikon çizgileri için morumsu mavi
    val placeholderColor = Color(0xFFAAAAAA) // Placeholder gri tonu

    Column(modifier = Modifier
        .fillMaxSize()
        .background(creamBg)
        .padding(24.dp)
        .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER (Geri Butonu ve Bildirim İkonu) ---
        Spacer(modifier = Modifier.height(20.dp)) // Üstten biraz boşluk
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // İki uca yasla
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Geri Butonu
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(limeGreen)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = purpleBorder,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Bildirim İkonu (Sağ Üst)
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = purpleBorder,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SELECT COURSE ---
        Text(
            text = "Select Course",
            color = darkPurple,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCourseTitle,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Courses", color = placeholderColor) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = purpleBorder,
                    unfocusedBorderColor = purpleBorder,
                    focusedContainerColor = inputFill,
                    unfocusedContainerColor = inputFill,
                    focusedTextColor = darkPurple,
                    unfocusedTextColor = darkPurple,
                    cursorColor = darkPurple
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                courseList.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.title, color = Color.Black) },
                        onClick = {
                            selectedCourseTitle = course.title
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- DESCRIPTION ---
        Text(
            text = "Description",
            color = darkPurple,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = descriptionText,
            onValueChange = { descriptionText = it },
            placeholder = { Text("Type..", color = placeholderColor) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp), // Görselde biraz daha yüksek duruyor
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = purpleBorder,
                unfocusedBorderColor = purpleBorder,
                focusedContainerColor = inputFill,
                unfocusedContainerColor = inputFill,
                focusedTextColor = darkPurple,
                unfocusedTextColor = darkPurple,
                cursorColor = darkPurple
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- ATTACHMENT (Kesikli Çizgili Alan) ---
        Text(
            text = "Attachment (optional)",
            color = darkPurple,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White) // İçi tam beyaz
                .clickable { launcher.launch("*/*") },
            contentAlignment = Alignment.Center
        ) {
            // Kesikli Çizgi (Border)
            if (selectedUri == null) {
                DashedBorderBox(color = purpleBorder)
            }

            // İçerik Gösterimi
            if (selectedUri != null) {
                // ... Mevcut dosya gösterme mantığı aynı kaldı ...
                if (mimeType.startsWith("image")) {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val icon = when {
                            mimeType.contains("pdf") -> Icons.Default.Description
                            mimeType.startsWith("audio") -> Icons.Default.Audiotrack
                            mimeType.startsWith("video") -> Icons.Default.Image
                            else -> Icons.Outlined.CloudUpload
                        }
                        Icon(icon, null, tint = darkPurple, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Attached File", fontWeight = FontWeight.Bold, color = darkPurple)
                    }
                }
                // Silme butonu
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { selectedUri = null; mimeType = "" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.Red)
                }
            } else {
                // Boş Durum (Yükleme yokken) - Görseldeki tasarım
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.CloudUpload,
                        contentDescription = null,
                        tint = purpleBorder,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Click to Upload File",
                        color = darkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Image, PDF, MP4 or Text",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Butonu en alta itmek için
        Spacer(modifier = Modifier.height(30.dp))

        // --- SHARE BUTTON ---
        Button(
            onClick = {
                if (selectedCourseTitle.isNotEmpty() && (selectedUri != null || descriptionText.isNotEmpty())) {
                    viewModel.uploadPost(selectedUri, selectedCourseTitle, descriptionText, context) {
                        navController.popBackStack()
                    }
                } else {
                    Toast.makeText(context, "Please select a course and add content.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = limeGreen,
                contentColor = darkPurple
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(color = darkPurple, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "SHARE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Görseldeki kesikli çizgi için (Tasarım aynı kaldı, renk parametresi güncellendi)
@Composable
fun DashedBorderBox(color: Color) {
    val stroke = Stroke(
        width = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = CornerRadius(16.dp.toPx())
        )
    }
}