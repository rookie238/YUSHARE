package com.example.yushare.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.yushare.R

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF6C7B8E)
            ),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(min = 500.dp, max = 650.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SETTINGS",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )

                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_x_close_icon),
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val menuItems = listOf(
                    "Edit Profile",
                    "Notification Prefrences",
                    "My Feedback Archive",
                    "Privacy and Visibility",
                    "Data Privacy Policy",
                    "Help & Support",
                    "Log Out"
                )

                menuItems.forEach { item ->
                    Column {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (item == "Edit Profile") {
                                        onEditProfileClick()
                                        onDismiss()
                                    } else {
                                        Toast.makeText(context, "$item seçildi", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(vertical = 16.dp)
                        )

                        if (item != "Log Out") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(alpha = 0.2f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color = color)
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsDialogPreview() {
    SettingsDialog(
        onDismiss = {},
        onEditProfileClick = {}
    )
}
