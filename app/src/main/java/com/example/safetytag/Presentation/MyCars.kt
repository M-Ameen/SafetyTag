package com.example.safetytag.Presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.safetytag.Model.Car
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.ViewModels.MyCarsViewModel

@Composable
fun MyCarsScreen(navController: NavController, sharedViewModel: MyCarsViewModel) {

    val viewModel = viewModel<MyCarsViewModel>()
    val loading = viewModel.loading


    LaunchedEffect(true) {
        viewModel.fetchDataFromFirebase()
    }

    // Observe the dataList from the ViewModel
    val dataList by rememberUpdatedState(newValue = viewModel.dataList)

    Column(modifier = Modifier) {
        CarRecordList(
            car = dataList, navController = navController, sharedViewModel = sharedViewModel
        )
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


@Composable
fun CarRecordList(
    car: List<Car>,
    modifier: Modifier = Modifier,
    navController: NavController,
    sharedViewModel: MyCarsViewModel
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = car) { car ->
            CarRecordListItem(
                car,
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}

@Composable
fun CarRecordListItem(
    car: Car, modifier: Modifier = Modifier,
    navController: NavController,
    sharedViewModel: MyCarsViewModel
) {
    val imagePainter = rememberImagePainter(
        data = car.QRCodeImgLink,
        builder = {
            // You can apply image loading options here if needed
            // For example: placeholder, error, transformations, etc.
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color(android.graphics.Color.parseColor("#D8D8D8"))),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .clickable {
                val car = Car(
                    userId = car.userId,
                    carName = car.carName,
                    carColor = car.carColor,
                    regNo = car.regNo,
                    phoneNo = car.phoneNo,
                    QRCodeImgLink = car.QRCodeImgLink
                )
                Log.d("UnTagScreen", car.toString())

                sharedViewModel.addCar(car)
                navController.navigate(Screen.AdTagScreen.route)
            }) {
            Image(
                painter = imagePainter,
                contentDescription = null, // Set a proper content description if needed
                modifier = Modifier
                    .size(100.dp) // Set an appropriate size for your image
                    .clip(shape = RectangleShape)

            )
            Column(modifier = Modifier.padding(start = 8.dp)) {

                Row() {

                    Text(text = "Car Name: ")
                    Text(
                        text = car.carName.toString()
                    )
                }
                Row() {

                    Text(text = "Car Color: ")
                    Text(
                        text = car.carColor.toString()
                    )
                }
                Row() {

                    Text(text = "Car RegNo: ")
                    Text(
                        text = car.regNo.toString()
                    )
                }
                Row() {

                    Text(text = "Owner PhoneNo: ")
                    Text(
                        text = car.phoneNo.toString()
                    )
                }
            }

        }
    }
}