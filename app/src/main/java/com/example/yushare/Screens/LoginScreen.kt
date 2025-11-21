package com.example.yushare.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yushare.R
import com.example.yushare.ui.theme.YUSHARETheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
    onLoginSuccess: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    val auth = if (!isPreview) FirebaseAuth.getInstance() else null
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.loginscreenbacground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Login içerikleri
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "YUSHARE",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.height(46.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 50.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (auth != null && db != null) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("LOGIN", "Giriş başarılı!")

                                        val uid = auth.currentUser!!.uid

                                        db.collection("users")
                                            .document(uid)
                                            .get()
                                            .addOnSuccessListener { doc ->
                                                val name = doc.getString("name")
                                                Log.d("FIRESTORE", "Kullanıcı adı: $name")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("FIRESTORE", "Hata: ${e.message}")
                                            }

                                        onLoginSuccess() // Compose Navigation ile yönlendir
                                    } else {
                                        loginError =
                                            task.exception?.message ?: "Giriş başarısız"
                                        Log.e(
                                            "LOGIN",
                                            "Giriş hatası: ${task.exception?.message}"
                                        )
                                    }
                                }
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("LOGIN")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    YUSHARETheme {
        LoginScreen(
            isPreview = true,
            onLoginSuccess = {} // Preview için boş lambda
        )
    }
}
