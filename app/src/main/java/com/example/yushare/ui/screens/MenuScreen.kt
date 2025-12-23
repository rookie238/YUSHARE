package com.example.yushare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R

val MenuBlue = Color(0xFF294BA3)

@Composable
fun MenuScreen(onCloseClick: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // 1. ANAHTARIMIZ BURADA
    var showPasswordPopup by remember { mutableStateOf(false) }
    var showNotificationPopup by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- TURUNCU ALAN ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(465.dp)
                        .background(
                            color = Color(0xFFFF9800),
                            shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(1.dp))

                        // Üst Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color(0xFFD9D9D9), androidx.compose.foundation.shape.CircleShape)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .clickable { onCloseClick() }
                                    .align(Alignment.CenterStart),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Back",
                                    tint =  Color.Black,

                                )

                            }
                            Text(
                                text = "My Profile",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MenuBlue,
                                fontFamily = poppinsFontFamily,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Spacer(modifier = Modifier.height(1.dp))

                        Image(
                            painter = painterResource(id = R.drawable.profile_avatar),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(280.dp)
                                .clip(RoundedCornerShape(283.dp))
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        Text(
                            text = "Arda Demir",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppinsFontFamily,
                            color = MenuBlue
                        )

                        Text(
                            text = "arda.demir@std.yeditepe.edu.tr",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFontFamily,
                            color = MenuBlue.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        Button(
                            onClick ={ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .width(126.dp)
                                .height(43.dp)
                        ) {
                            Text(
                                "Edit Profile",
                                color = MenuBlue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                // --- MENÜ LİSTESİ ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    MenuItem(
                        text = "Change Password",
                        onClick = { showPasswordPopup = true }
                    )

                    MenuItem(text = "Notification Preferences",
                        onClick = { showNotificationPopup = true }
                    )
                    MenuItem(text = "My Feedback Archive")
                    MenuItem(text = "Privacy and Visibility")
                    MenuItem(text = "Data Privacy Policy")
                    MenuItem(text = "Help & Support")

                    // LOGOUT BUTONU
                    Box(
                        modifier = Modifier
                            .width(366.dp)
                            .height(38.dp)
                            .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp))
                            .clickable {
                                (context as? android.app.Activity)?.finish()
                            }
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Logout",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color(0xFFBF0000)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.vector1243),
                                contentDescription = "Logout Arrow",
                                tint = Color(0xFFBF0000),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            //  ALT BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(MenuBlue)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(id = R.drawable.ic_nav_home), null, tint = Color.White.copy(0.6f), modifier = Modifier.size(30.dp))
                    Icon(painterResource(id = R.drawable.ic_nav_school), null, tint = Color.White.copy(0.6f), modifier = Modifier.size(34.dp))
                    Icon(painterResource(id = R.drawable.ic_nav_add), null, tint = Color.White.copy(0.6f), modifier = Modifier.size(40.dp))
                    Icon(painterResource(id = R.drawable.ic_nav_groups), null, tint = Color.White.copy(0.6f), modifier = Modifier.size(34.dp))
                    Icon(painterResource(id = R.drawable.ic_nav_profile), null, tint = Color.White, modifier = Modifier.size(30.dp))
                }
            }
        }

        // 3. POP-UP GÖRÜNTÜLEME ALANI
        if (showPasswordPopup) {
            ChangePasswordPopup(
                onDismiss = { showPasswordPopup = false }
            )
        }
            if (showNotificationPopup) {
                NotificationPopup(onDismiss = { showNotificationPopup = false })
            }
    }
}

@Composable
fun MenuItem(text: String, textColor: Color = Color(0xFF294BA3), onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .width(366.dp)
            .height(38.dp)
            .background(Color(0xFFD9D9D9), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

// --- CHANGE PASSWORD POP-UP TASARIMI ---
@Composable
fun ChangePasswordPopup(onDismiss: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(290.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF5A5A5A))
                .clickable(enabled = false) {}
                .padding(vertical = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text("Change Password",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFontFamily
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Your password at least must be at least 6 characters and should include a combination of numbers, letters and special characters(!@?*%).",
                    color = Color.White,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = poppinsFontFamily,
                    lineHeight = 16.sp,
                    modifier = Modifier.width(267.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 1. Mevcut Şifre
                RealPasswordInput(
                    text = currentPassword,
                    onValueChange = { currentPassword = it },
                    hint = "Current Password"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Yeni Şifre
                RealPasswordInput(
                    text = newPassword,
                    onValueChange = { newPassword = it },
                    hint = "New Password"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Tekrar Yeni Şifre
                RealPasswordInput(
                    text = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    hint = "Re-type new password"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9)),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(151.dp)
                        .height(29.dp)
                ) {
                    Text("Change Password",
                        color = Color(0xFF294BA3),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .size(24.dp)
            ) {

                Icon(painterResource(id = R.drawable.ic_x_close_icon),
                    "Close",
                    tint = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}


@Composable
fun RealPasswordInput(text: String, onValueChange: (String) -> Unit, hint: String) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = hint,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily
            )
        },
        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFD9D9D9).copy(alpha = 0.5f),
            unfocusedContainerColor = Color(0xFFD9D9D9).copy(alpha = 0.5f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(228.dp)
            .height(40.dp)
    )
}
// --- NOTIFICATION ---

@Composable
fun NotificationPopup(onDismiss: () -> Unit) {
    // TRANSFERS
    var uploadCompleted by remember { mutableStateOf(true) }
    var downloadCompleted by remember { mutableStateOf(false) }
    var newFileReceived by remember { mutableStateOf(false) }

    // GROUPS
    var groupUpload by remember { mutableStateOf(false) }
    var groupDownload by remember { mutableStateOf(false) }
    var groupNewFile by remember { mutableStateOf(false) }

    // GENERAL
    var appUpdates by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(290.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF5A5A5A))
                .clickable(enabled = false) {}
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // BAŞLIK
                Text(
                    text = "Notification Preferences",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- BÖLÜM 1: TRANSFERS ---
                SectionHeader(text = "TRANSFERS")
                NotificationSwitchRow(text = "Upload Completed", checked = uploadCompleted, onCheckedChange = { uploadCompleted = it })
                NotificationSwitchRow(text = "Download Completed", checked = downloadCompleted, onCheckedChange = { downloadCompleted = it })
                NotificationSwitchRow(text = "New File Received", checked = newFileReceived, onCheckedChange = { newFileReceived = it })

                Spacer(modifier = Modifier.height(15.dp))

                // --- BÖLÜM 2: GROUPS ---
                SectionHeader(text = "GROUPS")
                NotificationSwitchRow(text = "Upload Completed", checked = groupUpload, onCheckedChange = { groupUpload = it })
                NotificationSwitchRow(text = "Download Completed", checked = groupDownload, onCheckedChange = { groupDownload = it })
                NotificationSwitchRow(text = "New File Received", checked = groupNewFile, onCheckedChange = { groupNewFile = it })

                Spacer(modifier = Modifier.height(15.dp))

                // --- BÖLÜM 3: GENERAL ---
                SectionHeader(text = "GENERAL")
                NotificationSwitchRow(text = "App Updates", checked = appUpdates, onCheckedChange = { appUpdates = it })
            }

            // KAPATMA İKONU (X)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x_close_icon),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}

//  YARDIMCI BİLEŞENLER

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = poppinsFontFamily,
        modifier = Modifier.padding(bottom = 5.dp)
    )
}

@Composable
fun NotificationSwitchRow(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontFamily = poppinsFontFamily
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.scale(0.7f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen(onCloseClick = {})
}