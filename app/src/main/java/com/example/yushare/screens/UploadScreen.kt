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
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
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
import androidx.compose.ui.tooling.preview.Preview // EKLENDİ
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // EKLENDİ
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController // EKLENDİ
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
    var selectedCourseTitle by remember { mutableStateOf("") } // Burası String tutacak

    val darkPurple = Color(0xFF2B0B5E)
    val lightPurpleBg = Color(0xFFF3F0FF)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F7))) {
        // HEADER
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(darkPurple).padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White) }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Create Post", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())) {

            // --- DÜZELTİLEN DROPDOWN KISMI ---
            Text("Select Course", color = darkPurple, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedCourseTitle,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ders Seçiniz") },
                    placeholder = { Text("Listeden seçin") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkPurple, focusedLabelColor = darkPurple)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                    courseList.forEach { course ->
                        DropdownMenuItem(
                            // SADECE 'title' ÖZELLİĞİNİ YAZDIRIYORUZ:
                            text = { Text(course.title, color = Color.Black) },
                            onClick = {
                                // SEÇİLEN DEĞER SADECE DERSİN ADI OLUYOR (Örn: "VCD 301"):
                                selectedCourseTitle = course.title
                                expanded = false
                            }
                        )
                    }
                }
            }
            // ------------------------------------

            Spacer(modifier = Modifier.height(20.dp))

            Text("Description", color = darkPurple, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = descriptionText, onValueChange = { descriptionText = it },
                label = { Text("Açıklama yazın...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkPurple, focusedLabelColor = darkPurple)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Attachment (Optional)", color = darkPurple, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).clickable { launcher.launch("*/*") }, contentAlignment = Alignment.Center) {
                if (selectedUri == null) {
                    DashedBorderBox()
                }
                if (selectedUri != null) {
                    if (mimeType.startsWith("image")) {
                        AsyncImage(model = selectedUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize().background(lightPurpleBg)) {
                            val icon = when {
                                mimeType.contains("pdf") -> Icons.Default.Description
                                mimeType.startsWith("audio") -> Icons.Default.Audiotrack
                                mimeType.startsWith("video") -> Icons.Default.Image
                                else -> Icons.Default.InsertDriveFile
                            }
                            Icon(icon, null, tint = darkPurple, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Attached File", fontWeight = FontWeight.Bold, color = darkPurple, fontSize = 18.sp)
                            Text(mimeType, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp).size(32.dp).clip(CircleShape).background(Color.White).clickable { selectedUri = null; mimeType = "" }, contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Close, contentDescription = "Kaldır", tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, null, tint = darkPurple, modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Click to Upload File", color = darkPurple, fontWeight = FontWeight.Bold)
                        Text("Image, PDF, Audio or Text", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (selectedCourseTitle.isNotEmpty() && (selectedUri != null || descriptionText.isNotEmpty())) {
                        viewModel.uploadPost(selectedUri, selectedCourseTitle, descriptionText, context) {
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Lütfen bir ders seçin ve içerik ekleyin.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp).shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = darkPurple),
                shape = RoundedCornerShape(12.dp),
                enabled = !isUploading
            ) {
                if (isUploading) CircularProgressIndicator(color = Color.White) else Text("SHARE POST", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun DashedBorderBox() {
    val stroke = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f))
    val color = Color.Gray
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRoundRect(color = color, style = stroke, cornerRadius = CornerRadius(16.dp.toPx()))
    }
}

// --- EKLENEN PREVIEW KISMI ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UploadScreenPreview() {
    val navController = rememberNavController()
    // ViewModel'in init bloğundaki try-catch sayesinde preview'da Firebase hatası çökme yapmaz.
    // Ancak veriler boş gelir.
    val viewModel: SharedViewModel = viewModel()

    UploadScreen(viewModel = viewModel, navController = navController)
}