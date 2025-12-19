import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import com.example.yushare.R

val balooFont = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.baloo2_medium, FontWeight.Medium)
)

@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    currentBio: String
) {
    var bioText by remember { mutableStateOf(currentBio) }

    // --- 1. DIŞ KUTU (DIALOG) AYARLARI ---
    val dialogColor = Color(0xFF35414C).copy(alpha = 0.72f)
    val dialogWidth = 284.dp
    val dialogHeight = 408.dp
    val dialogRadius = 29.dp

    // --- 2. İÇ KUTU (BIO) AYARLARI ---
    val bioBoxColor = Color(0xFFECEBED).copy(alpha = 0.66f)
    val bioBoxWidth = 187.dp
    val bioBoxHeight = 170.dp
    val bioBoxRadius = 25.dp


    Dialog(
        onDismissRequest = { onDismiss() }

    ) {
        // --- ANA DIALOG KUTUSU ---
        Surface (
            modifier = Modifier
                .width(284.dp)   // Figma: 284
                .height(408.dp),// Figma: 408
                shape = RoundedCornerShape(29.dp), // Figma: 29
            color = Color(0xFF35414C).copy(alpha = 0.72f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Kapatma (X) Butonu - Sağ üst
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 20.dp)
                        .clickable { onDismiss() } // Tıklanabilir alan
                ) {
                    Text(
                        text = "x", // Figma'da bu bir ikon değil, "x" harfi
                        style = TextStyle(
                            fontFamily = balooFont,
                            fontWeight = FontWeight.SemiBold, // Figma: SemiBold
                            fontSize = 17.sp,                 // Figma: 17
                            color = Color.White,              // Renk: Beyaz
                            shadow = Shadow(                  // Efekt: Drop Shadow
                                color = Color.Black.copy(alpha = 0.25f),
                                offset = Offset(0f, 4f),
                                blurRadius = 4f
                            )
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { onDismiss() }
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                        .clickable { /* Resim Değiş */ }
                ) {
                    // YENİ: Beyaz Çerçeve (Arka Plan Dairesi)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = 90.dp, height = 88.55.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFECEBED).copy(alpha = 0.69f))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_avatar),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)

                    )
                        }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-4).dp, y = (-4).dp) // "Sol Üst"e doğru kaydırma (içeri çekme
                            .size(24.dp)       // Turkuaz dairenin boyutu
                            .clip(CircleShape)
                            .background(Color(0xFF01C8B3)) // FIGMA: Turkuaz Arka Plan
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Edit",
                            tint = Color(0xFF23006A), // FIGMA: Koyu Mor İkon
                            modifier = Modifier.size(16.dp) // İkon boyutu
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {}
                ) {
                    Text(
                        text = "Change Profile Picture",
                        style = TextStyle(
                            fontFamily =balooFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp) // Bio başlığıyla arasındaki mesafe
                            .clickable { /* Resim Değiş */ }
                        )
                    Spacer(modifier = Modifier.height(2.dp))

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bio Başlığı
                Text(
                    text = "Bio",
                    style = TextStyle(fontFamily = balooFont, color = Color.White, fontSize = 14.sp),
                    modifier = Modifier
                        .width(bioBoxWidth) // Başlık kutuyla aynı hizada başlasın
                        .padding(bottom = 4.dp)
                )

                // --- İÇ KUTU (BIO ALANI) ---
                Box(
                    modifier = Modifier
                        .width(bioBoxWidth)   // Figma: 187
                        .height(bioBoxHeight) // Figma: 170
                        .clip(RoundedCornerShape(bioBoxRadius)) // Figma: 25
                        .background(bioBoxColor)
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = bioText,
                        onValueChange = { bioText = it },
                        textStyle = TextStyle(
                            color = Color(0xFFFEFEFE), // Görsele göre beyaz duruyor
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium, // Figma Kalınlığı
                            fontFamily = balooFont
                        ),
                        modifier = Modifier.fillMaxSize()
                    )

                    if (bioText.isEmpty()) {
                        Text("Bio giriniz...", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileFigmaTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        EditProfileDialog(onDismiss = {}, currentBio = "FEIN FEIN FEIN")
    }
}
