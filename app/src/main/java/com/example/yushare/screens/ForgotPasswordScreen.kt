package com.example.yushare.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.yushare.R
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Arka Plan Resmi
        Image(
            painter = painterResource(id = R.drawable.loginscreenbackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ÜST KISIM
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f) // Ekranın üst kısmını kaplar
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Forgot Password Icon",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White // Renk güncellendi
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Forgot password?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Renk güncellendi
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            // ALT KISIM
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f), // Ekranın alt kısmını kaplar
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.Transparent // Arka planı transparan yapar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Don't worry! It happens. Please enter the email associated with your account.",
                        textAlign = TextAlign.Center,
                        color = Color.LightGray, // Renk güncellendi
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        placeholder = { Text("Enter your email adress") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFE9000),
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (auth != null && email.isNotEmpty()) {
                                isLoading = true
                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener {
                                        isLoading = false
                                        if (it.isSuccessful) {
                                            Toast.makeText(context, "Sıfırlama linki gönderildi!", Toast.LENGTH_LONG).show()
                                            navController.popBackStack()
                                        } else {
                                            val error = it.exception?.message ?: "Bir hata oluştu."
                                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)), // Mavi buton
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = "Send link", color = Color.White, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = buildAnnotatedString {
                            append("Remember password? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) { // Renk güncellendi
                                append("Sign in")
                            }
                        },
                        modifier = Modifier.clickable { navController.navigate("login") { popUpTo("forgot_password") { inclusive = true } } }
                    )
                }
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