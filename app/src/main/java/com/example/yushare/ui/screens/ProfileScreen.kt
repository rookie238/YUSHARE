package com.example.yushare.ui.screens

import EditProfileDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R

@Composable
fun ProfileScreen() {
    var showMenu by remember { mutableStateOf(false) }
    var showEditProfile by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Arka plan
        Image(
            painter = painterResource(id = R.drawable.profil_arkaplan),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Ana içerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))

            // Üst başlık + menü butonu
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PROFILE",
                    color = Color(0xFF23006A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default,
                    modifier = Modifier.align(Alignment.Center)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC0BFC4))
                        .align(Alignment.CenterEnd)
                        .clickable { showMenu = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF23006A),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Profil avatar + bilgiler
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_placeholder),
                            contentDescription = "Profil Avatarı",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // İsim + Department + Student Id
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 1.dp)
                ) {
                    Text(
                        text = "Arda Demir",
                        color = Color(0xFF23006A),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Default
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Row {
                        Text(
                            text = "Department: ",
                            color = Color(0xFF23006A),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default
                        )
                        Text(
                            text = "Visual Communication Design",
                            color = Color(0xFF23006A),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Row {
                        Text(
                            text = "Student Id: ",
                            color = Color(0xFF23006A),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default
                        )
                        Text(
                            text = "20210584978",
                            color = Color(0xFF23006A),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default
                        )
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))

                // Notes alanı
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Text(
                        text = "Notes:",
                        color = Color(0xFF23006A),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    var notesText by remember { mutableStateOf("") }

                    TextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        placeholder = {
                            Text("", color = Color.Gray)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.6f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                            disabledContainerColor = Color.White.copy(alpha = 0.6f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                }
            }
        }

        // --- ALT MENÜ BAŞLANGIÇ ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.BottomCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bottom_bar_bg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_groups),
                    contentDescription = "Group",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_custom),
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        // --- ALT MENÜ BİTİŞ ---
    } // end Box

    // 1. AYARLAR PENCERESİ
    if (showMenu) {
        SettingsDialog(
            onDismiss = { showMenu = false },
            onEditProfileClick = {
                showMenu = false       // Ayarlar menüsünü kapat
                showEditProfile = true // Edit Profile penceresini aç
            }
        )
    }

    // 2. EDIT PROFILE PENCERESİ (Arkası flu kalacak!)
    if (showEditProfile) {
        EditProfileDialog(
            onDismiss = { showEditProfile = false },
            currentBio = TODO() // Geri basınca kapat
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
