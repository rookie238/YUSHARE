package com.example.yushare.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

    // --- RENK PALETİ (Görsele Göre) ---
    val creamBg = Color(0xFFF0EFE9)       // Ana Arka Plan
    val limeGreen = Color(0xFFD2F066)     // Buton Rengi
    val darkBlue = Color(0xFF2B0B5E)      // Metin ve İkon Rengi
    val inputFill = Color(0xFFEBEBEB)     // Input Alanı İçi
    val blueBorder = Color(0xFF5F6AC4)    // Çerçeveler (Dashed line vb)
    // ----------------------------------

    Column(modifier = Modifier
        .fillMaxSize()
        .background(creamBg) // Arka plan rengi değişti
        .padding(20.dp)
        .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER (Geri Butonu) ---
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(limeGreen) // Geri butonu arka planı
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = blueBorder, // Ok rengi
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        // --- SELECT COURSE ---
        Text(
            text = "Select Course",
            color = darkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCourseTitle,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Courses") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // Köşeler yuvarlatıldı
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = blueBorder,
                    unfocusedBorderColor = blueBorder,
                    focusedContainerColor = inputFill,
                    unfocusedContainerColor = inputFill,
                    focusedLabelColor = darkBlue,
                    cursorColor = darkBlue
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

        Spacer(modifier = Modifier.height(20.dp))

        // --- DESCRIPTION ---
        Text(
            text = "Description",
            color = darkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = descriptionText,
            onValueChange = { descriptionText = it },
            placeholder = { Text("Type..") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = blueBorder,
                unfocusedBorderColor = blueBorder,
                focusedContainerColor = inputFill,
                unfocusedContainerColor = inputFill,
                cursorColor = darkBlue
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- ATTACHMENT ---
        Text(
            text = "Attachment (optional)",
            color = darkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White) // İçi beyaz
                .clickable { launcher.launch("*/*") },
            contentAlignment = Alignment.Center
        ) {
            // Kesikli Çizgi Çerçevesi
            if (selectedUri == null) {
                DashedBorderBox(color = blueBorder)
            }

            // İçerik
            if (selectedUri != null) {
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val icon = when {
                            mimeType.contains("pdf") -> Icons.Default.Description
                            mimeType.startsWith("audio") -> Icons.Default.Audiotrack
                            mimeType.startsWith("video") -> Icons.Default.Image
                            else -> Icons.Default.CloudUpload
                        }
                        Icon(icon, null, tint = darkBlue, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Attached File",
                            fontWeight = FontWeight.Bold,
                            color = darkBlue,
                            fontSize = 18.sp
                        )
                        Text(mimeType, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                // Silme butonu
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { selectedUri = null; mimeType = "" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Boş Durum (Yükleme yapılmamış)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CloudUpload, // Görseldeki bulut ikonuna benzer
                        null,
                        tint = blueBorder,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click to Upload File",
                        color = darkBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Image, PDF, MP4 or Text",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

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
                .height(60.dp), // Biraz daha yüksek
            colors = ButtonDefaults.buttonColors(
                containerColor = limeGreen, // Limon yeşili
                contentColor = darkBlue // Yazı rengi koyu
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(color = darkBlue)
            } else {
                Text(
                    text = "SHARE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

// Görseldeki gibi mavi kesikli çizgi için rengi parametre olarak alıyoruz
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