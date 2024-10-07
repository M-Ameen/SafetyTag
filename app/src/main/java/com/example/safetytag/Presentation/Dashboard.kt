package com.example.safetytag.Presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.R
import com.example.safetytag.ViewModels.SafetyTagViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Dashboard(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    val viewModel = viewModel<SafetyTagViewModel>()

    Box(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8")))
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Dashboard",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QRButton(qrIcon = R.drawable.qr_icon_scan, btnText = "Scan QR Code") {
                    navController.navigate(route = Screen.ScanQrCode.route)
                }
                QRButton(qrIcon = R.drawable.create_qr, btnText = "Create QR Code") {
                    navController.navigate(route = Screen.CreateQrCode.route)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QRButton(qrIcon = R.drawable.profile_icon, btnText = "Show My Cars") {
                    navController.navigate(route = Screen.MyCarsScreen.route)
                }
                QRButton(qrIcon = R.drawable.tag_icon, btnText = "Tagged Cars") {
                    navController.navigate(route = Screen.TagCarScreen.route)
                }
            }
        }

        // Image added at the top right corner
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logout_icon), // Replace with your image resource
                contentDescription = null, // Provide a suitable content description
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        showDialog = true
                    }
            )
            if (showDialog) {
                val context = LocalContext.current
                AlertDialog(onDismissRequest = { showDialog = false },
                    title = { Text(text = "Logout") },
                    text = { Text(text = "Are you sure you want to Logout?") },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.clearSharedPreferences(context,navController)
                            showDialog = false
                        }) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "Cancel")
                        }
                    })
            }

        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun QRButton(
    qrIcon: Int,
    btnText: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textStyle: TextStyle = TextStyle(),
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8")))
            .clickable {
                onClick()
            }
            .then(modifier)
    ) {
        Column() {
            Image(
                painter = painterResource(id = qrIcon), contentDescription = "1",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = btnText,
                style = textStyle,
                fontSize = 15.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}