package com.example.yushare.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yushare.R
import com.example.yushare.viewmodel.SharedViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

// Renk Tanımları
val MenuBlue = Color(0xFF294BA3)
val PopupBackground = Color(0xFF5A5A5A) // Görseldeki koyu gri renk
val SwitchGreen = Color(0xFF4CAF50) // Switch açık rengi

@Composable
fun MenuScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Verileri çekiyoruz
    val userProfile = viewModel.currentUserProfile.value
    val userEmail = viewModel.getCurrentUserEmail()

    // --- POPUP KONTROL DEĞİŞKENLERİ ---
    var showEditProfilePopup by remember { mutableStateOf(false) }
    var showPasswordPopup by remember { mutableStateOf(false) }
    var showNotificationPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --- TURUNCU HEADER KISMI ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .background(
                        color = Color(0xFFFF9800),
                        shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Geri Dön butonu ve Başlık
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 24.dp, end = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(0xFFD9D9D9), CircleShape)
                                .clip(CircleShape)
                                .clickable { navController.popBackStack() }
                                .align(Alignment.CenterStart),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "Back", tint = Color.Black)
                        }
                        Text(
                            text = "My Profile",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MenuBlue,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Profil Resmi
                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(vertical = 10.dp)
                            .clip(CircleShape)
                    )

                    // İsim ve Mail
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = userProfile.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MenuBlue
                        )
                        Text(
                            text = userEmail,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MenuBlue.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Edit Profile Butonu
                    Button(
                        onClick = { showEditProfilePopup = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .width(140.dp)
                            .height(45.dp)
                    ) {
                        Text("Edit Profile", color = MenuBlue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // --- MENÜ LİSTESİ ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuItem(text = "Change Password", onClick = { showPasswordPopup = true })
                MenuItem(text = "Notification Preferences", onClick = { showNotificationPopup = true })
                MenuItem(text = "My Feedback Archive")
                MenuItem(text = "Privacy and Visibility")
                MenuItem(text = "Help & Support")

                // Logout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp))
                        .clickable {
                            auth.signOut()
                            (context as? android.app.Activity)?.finish()
                        }
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFFBF0000))
                        Icon(painter = painterResource(id = R.drawable.vector), contentDescription = null, tint = Color(0xFFBF0000), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // --- POPUP YÖNETİMİ ---

        if (showEditProfilePopup) {
            EditProfilePopup(
                currentName = userProfile.name,
                currentBio = userProfile.bio,
                onDismiss = { showEditProfilePopup = false },
                onSave = { newName, newBio ->
                    viewModel.updateUserProfile(newName, newBio) { success ->
                        if (success) {
                            Toast.makeText(context, "Profil Güncellendi", Toast.LENGTH_SHORT).show()
                            showEditProfilePopup = false
                        }
                    }
                }
            )
        }

        if (showPasswordPopup) {
            ChangePasswordPopup(onDismiss = { showPasswordPopup = false })
        }

        if (showNotificationPopup) {
            NotificationPopup(onDismiss = { showNotificationPopup = false })
        }
    }
}

// ----------------------------------------------------------------
// --- ALT BİLEŞENLER (POPUP & ITEM) ---
// ----------------------------------------------------------------

@Composable
fun MenuItem(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = MenuBlue)
    }
}

@Composable
fun EditProfilePopup(
    currentName: String,
    currentBio: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var nameText by remember { mutableStateOf(currentName) }
    var bioText by remember { mutableStateOf(currentBio) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PopupBackground)
                .clickable(enabled = false) {}
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.profile_avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp).clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Name", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
                BasicTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD9D9D9))
                        .padding(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Bio", color = Color.White, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
                BasicTextField(
                    value = bioText,
                    onValueChange = { bioText = it },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD9D9D9))
                        .padding(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSave(nameText, bioText) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C9A7)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes", color = Color.White)
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-10).dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_x_close_icon), contentDescription = null, tint = Color.White)
            }
        }
    }
}

// --- NOTIFICATION POPUP (REVİZE EDİLDİ: ÇOK DAHA KÜÇÜK VE KOMPAKT) ---
@Composable
fun NotificationPopup(onDismiss: () -> Unit) {
    // State'ler
    var tUpload by remember { mutableStateOf(true) }
    var tDownload by remember { mutableStateOf(false) }
    var tNewFile by remember { mutableStateOf(false) }

    var gUpload by remember { mutableStateOf(false) }
    var gDownload by remember { mutableStateOf(false) }
    var gNewFile by remember { mutableStateOf(false) }

    var appUpdates by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(280.dp) // GÖRSELDEKİ GİBİ DARALTILDI (Eskisi 320dp idi)
                .clip(RoundedCornerShape(20.dp))
                .background(PopupBackground)
                .clickable(enabled = false) {}
                .padding(16.dp) // PADDING AZALTILDI (Daha sıkışık görünüm)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // BAŞLIK ve X BUTONU
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Notification Preferences",
                        color = Color.White,
                        fontSize = 15.sp, // FONT KÜÇÜLTÜLDÜ
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x_close_icon),
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp) // İKON KÜÇÜLTÜLDÜ
                            .align(Alignment.CenterEnd)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(15.dp)) // Boşluk azaltıldı

                // --- SECTIONS ---
                SectionHeader(text = "TRANSFERS")
                NotificationSwitchRow(text = "Upload Completed", checked = tUpload) { tUpload = it }
                NotificationSwitchRow(text = "Download Completed", checked = tDownload) { tDownload = it }
                NotificationSwitchRow(text = "New File Received", checked = tNewFile) { tNewFile = it }

                Spacer(modifier = Modifier.height(10.dp)) // Boşluk azaltıldı

                SectionHeader(text = "GROUPS")
                NotificationSwitchRow(text = "Upload Completed", checked = gUpload) { gUpload = it }
                NotificationSwitchRow(text = "Download Completed", checked = gDownload) { gDownload = it }
                NotificationSwitchRow(text = "New File Received", checked = gNewFile) { gNewFile = it }

                Spacer(modifier = Modifier.height(10.dp)) // Boşluk azaltıldı

                SectionHeader(text = "GENERAL")
                NotificationSwitchRow(text = "App Updates", checked = appUpdates) { appUpdates = it }

                // Alt kısımda gereksiz boşluk bırakmadık
            }
        }
    }
}

// --- CHANGE PASSWORD POPUP ---
@Composable
fun ChangePasswordPopup(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(320.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PopupBackground)
                .clickable(enabled = false) {}
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Change Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your password at least must be at least 6 characters and should include a combination of numbers, letters and special characters(!@?*%).",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                StyledPasswordField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    placeholder = "Current Password"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StyledPasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = "New Password"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StyledPasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Re-type new password"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                            if (newPassword != confirmPassword) {
                                Toast.makeText(context, "Yeni şifreler eşleşmiyor.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (newPassword.length < 6) {
                                Toast.makeText(context, "Şifre en az 6 karakter olmalı.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            val user = auth.currentUser
                            val userEmail = user?.email

                            if (user != null && userEmail != null) {
                                val credential = EmailAuthProvider.getCredential(userEmail, oldPassword)
                                user.reauthenticate(credential)
                                    .addOnSuccessListener {
                                        user.updatePassword(newPassword)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                Toast.makeText(context, "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_LONG).show()
                                                onDismiss()
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                Toast.makeText(context, "Güncelleme Hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        Toast.makeText(context, "Mevcut şifreniz yanlış.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = PopupBackground, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Change Password", color = PopupBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- YARDIMCI BİLEŞENLER (REVİZE EDİLDİ - DAHA KÜÇÜK) ---

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 12.sp, // Header fontu küçültüldü
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp) // Alt boşluk azaltıldı
    )
}

@Composable
fun NotificationSwitchRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp), // Dikey padding sıfırlandı, daha sıkışık olması için
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp, // Yazı fontu küçültüldü
            fontWeight = FontWeight.Medium
        )
        // Switch'i küçültmek için scale kullanıyoruz
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SwitchGreen,
                checkedTrackColor = SwitchGreen.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.Gray
            ),
            modifier = Modifier.scale(0.7f) // %70 boyutuna küçültüldü
        )
    }
}

@Composable
fun StyledPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
        visualTransformation = PasswordVisualTransformation(),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp)
                }
                innerTextField()
            }
        }
    )
}