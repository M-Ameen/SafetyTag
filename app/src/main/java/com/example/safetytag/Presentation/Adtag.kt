package com.example.safetytag.Presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.safetytag.R
import com.example.safetytag.ViewModels.MyCarsViewModel

@Composable
fun AdtagScreen(navController: NavController, sharedViewModel: MyCarsViewModel, context: Context) {
    val viewModel: MyCarsViewModel = viewModel()

    val car = sharedViewModel.car
    val tagCar = viewModel.tagCar

    LaunchedEffect(key1 = car) {
        if (car != null) {
            Log.d("AdTagScreen", car.carName.toString())
            Log.d("AdTagScreen", car.carColor.toString())
        }
    }

    // Observe the phoneNumberState from the ViewModel
    var carName = viewModel.carNameState.value
    var carColor = viewModel.carColorState.value
    var carRegNo = viewModel.carRegNoState.value
    var phoneNumber = viewModel.phoneNumberState.value

    var carLocation = viewModel.carTagLocation.value

    LaunchedEffect(car) {
        if (car != null) {
            viewModel.phoneNumberState.value = car.phoneNo.toString()
            viewModel.userIdState.value = car.userId.toString()
            viewModel.carNameState.value = car.carName.toString()
            viewModel.carColorState.value = car.carColor.toString()
            viewModel.carRegNoState.value = car.regNo.toString()
        }

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
            text = "Ad Tag", modifier = Modifier, fontSize = 25.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(50.dp))
        TF(
            label = R.string.carname,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
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
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
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
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
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
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            value = phoneNumber,
            onValueChanged = { viewModel.phoneNumberState.value = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.carlocation,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            value = carLocation,
            onValueChanged = { viewModel.carTagLocation.value = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            viewModel.createTag(
                context = context,
                carName = carName,
                carColor = carColor,
                regNo = carRegNo,
                phoneNo = phoneNumber,
                QRCodeImgLink = car?.QRCodeImgLink.toString(),
            )
        }) {
            Text(
                text = stringResource(R.string.carlocation), fontSize = 16.sp
            )
        }
//        Button(modifier = Modifier.fillMaxWidth(), onClick = {
//            viewModel.createTag(
//                carName = carName,
//                carColor = carColor,
//                regNo = carRegNo,
//                phoneNo = phoneNumber,
//                QRCodeImgLink = car?.QRCodeImgLink.toString(),
//                carLocation = carLocation,
//            )
//        }) {
//            Text(
//                text = stringResource(R.string.create_tag), fontSize = 16.sp
//            )
//        }
    }
    if (tagCar) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            val context = LocalContext.current
            Toast.makeText(context, "Car Location Saved", Toast.LENGTH_SHORT).show()
        }
    }
}