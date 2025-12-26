package com.example.yushare.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    isPreview: Boolean = false
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val auth = if (!isPreview) FirebaseAuth.getInstance() else null

    // Görseldeki Renk Paleti
    val OrangeColor = Color(0xFFFF9800) // Canlı Turuncu
    val ButtonBlue = Color(0xFF3F51B5)  // Koyu Mavi
    val TextColorDark = Color(0xFF1D1D1D) // Koyu Siyah

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- ÜST KISIM (BEYAZ ALAN) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f) // Ekranın %45'i
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Kilit İkonu
            Icon(
                imageVector = Icons.Outlined.Lock, // İçi boş kilit ikonu (varsa)
                contentDescription = "Forgot Password Icon",
                modifier = Modifier.size(80.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Başlık
            Text(
                text = "Forgot password?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // --- ALT KISIM (TURUNCU ALAN) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f) // Ekranın %55'i
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) // Yuvarlak üst köşeler
                .background(OrangeColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp), // İçerik boşluğu
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Açıklama Metni
                Text(
                    text = "Don't worry! It happens. Please enter the email associated with your account.",
                    textAlign = TextAlign.Start, // Görselde sola yaslı gibi duruyor veya ortalı
                    color = Color(0xFF333333), // Koyu gri, turuncu üstünde okunur
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Email Input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    placeholder = { Text("Enter your email address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        // Odaklanıldığında
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        focusedTextColor = Color.Black,
                        // Odaklanılmadığında
                        unfocusedBorderColor = Color.DarkGray,
                        unfocusedLabelColor = Color.DarkGray,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        // Arka planı transparan bırakıyoruz ki turuncu görünsün
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Gönder Butonu
                Button(
                    onClick = {
                        if (auth != null && email.isNotEmpty()) {
                            isLoading = true
                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener {
                                    isLoading = false
                                    if (it.isSuccessful) {
                                        Toast.makeText(context, "Reset link sent!", Toast.LENGTH_LONG).show()
                                        navController.popBackStack()
                                    } else {
                                        val error = it.exception?.message ?: "An error has occurred."
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(30.dp), // Buton daha yuvarlak (capsule)
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Send link",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Alt Link (Sign In)
                Text(
                    text = buildAnnotatedString {
                        append("Remember password? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                            append("Sign in")
                        }
                    },
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("login") {
                                popUpTo("forgot_password") { inclusive = true }
                            }
                        }
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        navController = rememberNavController(),
        isPreview = true
    )
}