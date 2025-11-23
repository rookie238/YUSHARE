import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    currentBio: String
) {
    var bioText by remember { mutableStateOf(currentBio) }

    // --- 1. DIŞ KUTU (DIALOG) AYARLARI ---
    val dialogColor = Color(0xFF35414C).copy(alpha = 0.72f) // %72 Opaklık
    val dialogWidth = 284.dp
    val dialogHeight = 408.dp
    val dialogRadius = 29.dp

    // --- 2. İÇ KUTU (BIO) AYARLARI ---
    val bioBoxColor = Color(0xFFECEBED).copy(alpha = 0.66f) // %66 Opaklık
    val bioBoxWidth = 187.dp
    val bioBoxHeight = 170.dp
    val bioBoxRadius = 25.dp

    // --- 3. DİĞER RENKLER ---
    val plusIconColor = Color(0xFF311B92) // Koyu Mor (Tahmini, sonra sabitleriz)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // --- ANA DIALOG KUTUSU ---
        Box(
            modifier = Modifier
                .width(dialogWidth)   // Figma: 284
                .height(dialogHeight) // Figma: 408
                .clip(RoundedCornerShape(dialogRadius)) // Figma: 29
                .background(dialogColor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Kapatma (X) Butonu - Sağ üst
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 16.dp), // Kenar boşlukları
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {}
                ) {
                    Text(
                        text = "Change Profile Picture",
                        fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif, // Baloo 2 ekleyince burayı değiştiririz
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.White
                        )
                    Spacer(modifier = Modifier.height(2.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF311B92),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bio Başlığı
                Text(
                    text = "Bio",
                    style = TextStyle(color = Color.White, fontSize = 14.sp),
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
                            color = Color.White, // Görsele göre beyaz duruyor
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
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