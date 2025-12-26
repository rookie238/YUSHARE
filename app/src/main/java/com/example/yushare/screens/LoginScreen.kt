package com.example.yushare.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.yushare.ui.theme.YUSHARETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.yushare.R

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Preview modunda Firebase'i başlatma, yoksa hata verir.
    val auth = if (!isPreview) FirebaseAuth.getInstance() else null
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    val OrangeColor = Color(0xFFFF9800)
    val DarkBlueColor = Color(0xFF304FFE)
    val TextColor = Color(0xFF333333)

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loginscreenbackground),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Surface(
                modifier = Modifier.weight(0.65f).fillMaxWidth(),
                color = OrangeColor,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        label = { Text("E-mail") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor, unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor, unfocusedLabelColor = TextColor
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password, onValueChange = { password = it },
                        label = { Text("Password") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor, unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor, unfocusedLabelColor = TextColor
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // --- SOL TARAF: Remember Me ---
                        Row(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .clickable { isRememberMe = !isRememberMe },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isRememberMe) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = DarkBlueColor // Eğer hata verirse Color(0xFF294BA3) kullanabilirsin
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Remember me", fontSize = 14.sp, color = TextColor)
                        }

                        // --- SAĞ TARAF: Forgot Password ---
                        Text(
                            text = "Forgot Password?",
                            fontSize = 14.sp,
                            color = Black, // Veya kendi renk kodunu kullan
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .clickable {
                                    // Şifremi unuttum sayfasına yönlendirme
                                    navController.navigate("forgot_password")
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                val auth = FirebaseAuth.getInstance()
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            // --- EKLENEN KISIM BAŞLANGIÇ ---
                                            // Tercihi SharedPreferences'a kaydet
                                            val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                            val editor = sharedPref.edit()
                                            if (isRememberMe) {
                                                editor.putBoolean("remember_me", true)
                                            } else {
                                                editor.putBoolean("remember_me", false)
                                                // İsteğe bağlı: İşaretli değilse çıkış yapılmış sayılabilir ama
                                                // genelde sadece "beni hatırlama" flag'i false yapılır.
                                            }
                                            editor.apply()
                                            // --- EKLENEN KISIM BİTİŞ ---

                                            onLoginSuccess() // Ana ekrana yönlendir
                                        } else {
                                            Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlueColor),
                        shape = RoundedCornerShape(25.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else Text(text = "Sign in", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Register using your Google account!", fontSize = 14.sp, color = TextColor)

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = "Google Sign In",
                        modifier = Modifier
                            .size(75.dp)
                            .clickable { Toast.makeText(context, "Google Sign In Clicked", Toast.LENGTH_SHORT).show() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Don't have an account? ", fontSize = 14.sp, color = TextColor)
                        Text(
                            text = "Sign up",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                navController.navigate("register")
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- PREVIEW EKLENTİSİ BURADA ---
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    YUSHARETheme {
        LoginScreen(
            navController = rememberNavController(),
            isPreview = true, // Firebase hatalarını önlemek için true olmalı
            onLoginSuccess = {}
        )
    }
}