package com.example.yushare.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning // Hata ikonu için
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.yushare.R
import com.example.yushare.ui.theme.YUSHARETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun RegisterScreen(
    navController: NavController,
    isPreview: Boolean = false
) {

    
    // Form alanları
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Checkbox durumu
    var isTermsAccepted by remember { mutableStateOf(false) }

    // Hata mesajı görünürlüğü
    var showErrorMessage by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val auth = if (!isPreview) FirebaseAuth.getInstance() else null
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    // Tasarımdaki Renkler
    val OrangeColor = Color(0xFFFF9800) // Arka plan turuncusu
    val DarkBlueColor = Color(0xFF3F51B5) // Sign up butonu mavisi
    val TextColor = Color(0xFF333333)
    val ErrorColor = Color.Red

    Box(modifier = Modifier.fillMaxSize()) {

        // Arka plan resmi (varsa)
        Image(
            painter = painterResource(id = R.drawable.loginscreenbackground),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // --- ÜST KISIM ---
            Column(
                modifier = Modifier
                    .weight(0.25f) // Üst alan oranı
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // İkon (Person +)
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Register Icon",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                // "Let's you in" Yazısı
                Text(
                    text = "Get started!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // --- ALT KISIM (Turuncu Form) ---
            Surface(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth(),
                color = OrangeColor,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()), // Ekran küçükse kaydırma özelliği
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. NAME
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor,
                            unfocusedLabelColor = TextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. LAST NAME
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor,
                            unfocusedLabelColor = TextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. E-MAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor,
                            unfocusedLabelColor = TextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 4. PASSWORD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = TextColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.5f),
                            focusedLabelColor = TextColor,
                            unfocusedLabelColor = TextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // TERMS CHECKBOX
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTermsAccepted,
                            onCheckedChange = { isTermsAccepted = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DarkBlueColor,
                                uncheckedColor = TextColor
                            )
                        )
                        val termsText = buildAnnotatedString {
                            append("You accept the terms and conditions? ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                                append("Terms")
                            }
                        }
                        Text(text = termsText, fontSize = 12.sp, color = TextColor)
                    }

                    // HATA MESAJI (Enter all fields!)
                    if (showErrorMessage) {
                        Text(
                            text = "Enter all fields!",
                            color = ErrorColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 4.dp).align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // SIGN UP BUTTON
                    Button(
                        onClick = {
                            // Validasyon Kontrolü
                            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || !isTermsAccepted) {
                                showErrorMessage = true
                            } else {
                                showErrorMessage = false
                                // Kayıt İşlemleri
                                if (auth != null && db != null) {
                                    isLoading = true
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener { authResult ->
                                            val uid = authResult.user?.uid
                                            if (uid != null) {
                                                val fullName = "$firstName $lastName"
                                                val userMap = hashMapOf(
                                                    "uid" to uid,
                                                    "name" to fullName,
                                                    "firstName" to firstName,
                                                    "lastName" to lastName,
                                                    "email" to email,
                                                    "createdAt" to System.currentTimeMillis()
                                                )

                                                db.collection("users").document(uid).set(userMap)
                                                    .addOnSuccessListener {
                                                        isLoading = false
                                                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                                        navController.navigate("home") {
                                                            popUpTo("login") { inclusive = true }
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        isLoading = false
                                                        Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            Toast.makeText(context, "Registration Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlueColor),
                        shape = RoundedCornerShape(25.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = "Sign up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // GOOGLE REGISTER TEXT
                    Text(
                        text = "Register using your Google account!",
                        fontSize = 12.sp,
                        color = TextColor.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // GOOGLE ICON (Placeholder)
                    // Gerçek resim için res/drawable klasörüne 'google_logo' ekleyin ve aşağıdaki yorumu açın:

                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = "Google Sign In",
                        modifier = Modifier
                            .size(75.dp)
                            .clickable { Toast.makeText(context, "Google Sign In Clicked", Toast.LENGTH_SHORT).show() }
                    )




                    Spacer(modifier = Modifier.weight(1f))

                    // FOOTER (Already have account?)
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Already have an aacount? ", fontSize = 14.sp, color = TextColor)
                        Text(
                            text = "Sign in",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black, // Siyah renk
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    YUSHARETheme {
        RegisterScreen(
            navController = rememberNavController(),
            isPreview = true, // Firebase hatalarını önlemek için true olmalı

        )
    }
}