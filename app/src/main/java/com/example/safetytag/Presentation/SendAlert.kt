package com.example.safetytag.Presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.safetytag.R
import com.example.safetytag.ViewModels.SendAlertViewModel

@Composable
fun SendAlertScreen(navController: NavController, qr_code_result: String, context: Context) {


    val viewModel: SendAlertViewModel = viewModel()

    // Observe the phoneNumberState from the ViewModel
    var carName = viewModel.carNameState.value
    var carColor = viewModel.carColorState.value
    var carRegNo = viewModel.carRegNoState.value
    var phoneNumber = viewModel.phoneNumberState.value
    var selectedCarIssue by remember { mutableStateOf("") }

    LaunchedEffect(qr_code_result) {
        viewModel.phoneNumberState.value = extractPhoneNumber(qr_code_result)
        viewModel.userIdState.value = extractUserId(qr_code_result)
        viewModel.carNameState.value = extractCarName(qr_code_result)
        viewModel.carColorState.value = extractCarColor(qr_code_result)
        viewModel.carRegNoState.value = extractRegNo(qr_code_result)
    }


    Column(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8")))
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Send Alert",
            modifier = Modifier,
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
            onValueChanged = { viewModel.carNameState.value = it },
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
            onValueChanged = { viewModel.carColorState.value = it },
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
            onValueChanged = { viewModel.carRegNoState.value = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.phonenno,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = phoneNumber,
            onValueChanged = { viewModel.phoneNumberState.value = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        Spinner(selectedOption = selectedCarIssue) { newOption ->
            selectedCarIssue = newOption
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.sendAlert(
                    context = context,
                    carName = carName,
                    regNo = carRegNo,
                    issue = selectedCarIssue,
                    phoneNumber = phoneNumber
                )
            }
        ) {
            Text(
                text = stringResource(R.string.send_alert),
                fontSize = 16.sp
            )
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spinner(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val list = listOf<String>("Petrol Leakage", "Wrong Parking", "Tire Puncture")
    var expanded by remember { mutableStateOf(false) }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column(modifier = Modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                },
            label = {
                Text(text = "Car Issue")
            },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) {
                    textFiledSize.width.toDp()
                }
            )
        ) {
            list.forEach { label ->
                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                    onOptionSelected(label)
                    expanded = false
                })
            }
        }
    }
}


fun extractPhoneNumber(qrCodeResult: String): String {
    // Assuming the phone number is in the format "phoneNo=03056666666"
    val phoneNumberRegex = """phoneNo=([\d]+)""".toRegex()
    val matchResult = phoneNumberRegex.find(qrCodeResult)

    return if (matchResult != null && matchResult.groupValues.size >= 2) {
        matchResult.groupValues[1]
    } else {
        "Phone number not found"
    }
}


fun extractUserId(qrCodeResult: String): String {
    // Assuming the userId is in the format "userId=J0JQVUpWjAVDelKaG6QiG51ISz92"
    val userIdRegex = """userId=([^,]+)""".toRegex()
    val matchResult = userIdRegex.find(qrCodeResult)

    return if (matchResult != null && matchResult.groupValues.size >= 2) {
        matchResult.groupValues[1]
    } else {
        "User ID not found"
    }
}

fun extractCarName(qrCodeResult: String): String {
    // Assuming the carName is in the format "carName=Baleno"
    val carNameRegex = """carName=([^,]+)""".toRegex()
    val matchResult = carNameRegex.find(qrCodeResult)

    return if (matchResult != null && matchResult.groupValues.size >= 2) {
        matchResult.groupValues[1]
    } else {
        "Car Name not found"
    }
}

fun extractCarColor(qrCodeResult: String): String {
    // Assuming the carColor is in the format "carColor=Black"
    val carColorRegex = """carColor=([^,]+)""".toRegex()
    val matchResult = carColorRegex.find(qrCodeResult)

    return if (matchResult != null && matchResult.groupValues.size >= 2) {
        matchResult.groupValues[1]
    } else {
        "Car Color not found"
    }
}

fun extractRegNo(qrCodeResult: String): String {
    // Assuming the regNo is in the format "regNo=LOD 1125"
    val regNoRegex = """regNo=([^,]+)""".toRegex()
    val matchResult = regNoRegex.find(qrCodeResult)

    return if (matchResult != null && matchResult.groupValues.size >= 2) {
        matchResult.groupValues[1]
    } else {
        "Registration Number not found"
    }
}
