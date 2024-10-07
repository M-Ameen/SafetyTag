package com.example.safetytag.ViewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SendAlertViewModel() : ViewModel() {
    val phoneNumberState = mutableStateOf("")

    // State to hold the userId
    val userIdState = mutableStateOf("")

    // State to hold the car name
    val carNameState = mutableStateOf("")

    // State to hold the car color
    val carColorState = mutableStateOf("")

    // State to hold the registration number
    val carRegNoState = mutableStateOf("")


    fun sendAlert(context: Context, carName: String, regNo: String, issue: String, phoneNumber: String) {
        val message = "Hello, your $carName car with Registration Number $regNo is having this issue $issue"
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", message)
        context.startActivity(intent)
    }

    fun setPhoneNumber(phoneNumber: String) {
        phoneNumberState.value = phoneNumber
    }
}