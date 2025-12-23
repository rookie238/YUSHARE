package com.example.yushare.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview // Preview için gerekli import
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController // Preview'da navController için gerekli import
import com.example.yushare.R
import com.example.yushare.ui.theme.YUSHARETheme // Temanın doğru import edildiğinden emin ol
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController) {
    // 2 Sayfalık yapı
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Arka Plan Görselleri (drawable klasöründe onboard1 ve onboard2 olmalı)
                // Not: Preview'da resimler görünmeyebilir veya gri alan çıkabilir, emülatörde düzelir.
                Image(
                    painter = painterResource(
                        id = if (page == 0) R.drawable.onboard1 else R.drawable.onboard2
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Buton Alanı
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (page == 0) {
                        // 1. Sayfa Butonu (Solda)
                        Button(
                            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .height(50.dp)
                                .width(150.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Get Started", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // 2. Sayfa Butonu (Sağda) -> Login'e gider
                        Button(
                            onClick = {
                                navController.navigate("login") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .height(50.dp)
                                .width(120.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Next", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEW KISMI ---
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    YUSHARETheme {
        // Preview modunda sahte bir navController oluşturuyoruz
        OnboardingScreen(navController = rememberNavController())
    }
}