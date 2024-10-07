package com.example.safetytag.Presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.safetytag.R
import com.example.safetytag.ViewModels.GenerateQRViewModel
import com.example.safetytag.ui.theme.SafetyTagTheme

@Composable
fun CreateQRCodeScreen(navController: NavHostController) {
    val viewModel: GenerateQRViewModel = viewModel()
    val loading = viewModel.loading

    // Observe changes to the bitmap using rememberUpdatedState
    val qrCodeBitmap by rememberUpdatedState(newValue = viewModel.qrCodeBitmap.value)

    var carName by remember { mutableStateOf("") }
    var carColor by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var carRegNo by remember { mutableStateOf("") }
    val context= LocalContext.current
    val state = viewModel.state
    Column(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8")))
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Generate QR Code",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(50.dp))
        TF(
            label = R.string.carname,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = carName,
            onValueChanged = { carName = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.carcolor,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = carColor,
            onValueChanged = { carColor = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.carregno,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = carRegNo,
            onValueChanged = { carRegNo = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.phonenno,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = phoneNo,
            onValueChanged = { phoneNo = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        // Display the QR code bitmap if available
        qrCodeBitmap?.let { bitmap ->
            Image(
                bitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(300.dp)
            )
        } ?: run {
            // Placeholder or default content if the bitmap is not available
            // You can show a loading indicator or a placeholder image here
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.generateQrCode(
                    carName = carName,
                    carColor = carColor,
                    regNo = carRegNo,
                    phoneNo = phoneNo,
                    context = context
                )
//            viewModel.generatqr()
            }
        ) {
            Text(
                text = stringResource(R.string.generate),
                fontSize = 16.sp
            )
        }
    }
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SafetyTagTheme {
        CreateQRCodeScreen(navController = rememberNavController())
    }
}