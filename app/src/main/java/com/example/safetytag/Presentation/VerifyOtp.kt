package com.example.safetytag.Presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.R
import com.example.safetytag.ViewModels.SafetyTagViewModel

@Composable
fun VerifyOTPLayout(navController:NavController) {
    var otpInput by remember { mutableStateOf("") }
    val viewModel = viewModel<SafetyTagViewModel>()
    val state = viewModel.state

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Verify OTP",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(50.dp))
        TF(
            label = R.string.enterotp,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = otpInput,
            onValueChanged = { otpInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
//                viewModel.verifyOtp(otpInput)
                navController.navigate(route = Screen.DashboardScreen.route)

            }
        ) {
            Text(
                text = stringResource(R.string.verifyotp),
                fontSize = 16.sp
            )
    }
}}