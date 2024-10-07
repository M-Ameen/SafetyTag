package com.example.safetytag.Presentation

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.safetytag.Model.TagCar
import com.example.safetytag.R
import com.example.safetytag.ViewModels.MyCarsViewModel

@Composable
fun TagCarScreen(navController: NavController, context: Context) {
    val viewModel = viewModel<MyCarsViewModel>()
    val loading = viewModel.loading


    LaunchedEffect(true) {
        viewModel.fetchDataTaggedcarsFromFirebase()
    }

    // Observe the dataList from the ViewModel
    val dataList by rememberUpdatedState(newValue = viewModel.taggedcardataList)

    Column(modifier = Modifier) {
        TaggedCarRecordList(
            car = dataList, context = context
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
fun TaggedCarRecordList(
    car: List<TagCar>, modifier: Modifier = Modifier, context: Context
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = car) { car ->
            TaggedCarRecordListItem(car, context = context)
        }
    }
}

@Composable
fun TaggedCarRecordListItem(car: TagCar, modifier: Modifier = Modifier, context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    val viewModel = viewModel<MyCarsViewModel>()
    val imagePainter = rememberImagePainter(data = car.QRCodeImgLink)
    Column(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8"))),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth(1f)
            .padding(start = 8.dp, top = 8.dp)
            .clickable {
                val (latitude, longitude) = extractLatitudeAndLongitude(car.carLocation.toString())
                Log.d("carlocation", "Latitude: $latitude, Longitude: $longitude")
                viewModel.openGoogleMaps(
                    context = context,
                    latitude = latitude,
                    longitude = longitude
                )
            }) {
            Image(
                painter = imagePainter,
                contentDescription = null, // Set a proper content description if needed
                modifier = Modifier
                    .size(100.dp) // Set an appropriate size for your image
                    .clip(shape = RectangleShape) // Clip the image if needed
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
                Row() {

                    Text(text = "Car Location: ")
                    Text(
                        text = car.carLocation.toString(),
                        fontSize = 15.sp,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.option_icon),
                contentDescription = null, // Set a proper content description if needed
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        showDialog = true
                    },
                alignment = Alignment.TopEnd,
            )
            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false },
                    title = { Text(text = "UnTag Car") },
                    text = { Text(text = "Are you sure you want to untag this car?") },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.deleteTaggedCarByChildId(
                                car.userId.toString(),
                                car.regNo.toString()
                            )
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

fun extractLatitudeAndLongitude(locationString: String): Pair<Double, Double> {
    val parts = locationString.split(",")

    val latitude = parts[0].toDouble()
    val longitude = parts[1].toDouble()

    return latitude to longitude
}
