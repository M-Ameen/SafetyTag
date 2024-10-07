package com.example.safetytag

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.safetytag.ViewModels.MyCarsViewModel
import com.example.safetytag.ViewModels.SendAlertViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestingIntents {


    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSendAlertImplicitIntent() {
        val viewModel = SendAlertViewModel()
        composeTestRule.setContent {
            context = LocalContext.current
        }

        val carName = "Sample Car"
        val regNo = "ABC123"
        val issue = "Engine Trouble"
        val phoneNumber = "1234567890"

        viewModel.sendAlert(context, carName, regNo, issue, phoneNumber)

        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_SENDTO))
        Intents.intended(IntentMatchers.hasData(Uri.parse("smsto:$phoneNumber")))

        val sentIntents = Intents.getIntents()
        val intent = sentIntents.find { it.action == Intent.ACTION_SENDTO }!!

        val expectedMessage =
            "Hello, your $carName car with Registration Number $regNo is having this issue $issue"
        val actualMessage = intent.getStringExtra("sms_body")

        assert(actualMessage == expectedMessage)
    }

    @Test
    fun testOpenGoogleMapsImplicitIntent() {
        val viewModel = MyCarsViewModel()

        val latitude = 37.7749
        val longitude = -122.4194
        composeTestRule.setContent {
            context = LocalContext.current
        }
        viewModel.openGoogleMaps(
            context,
            latitude,
            longitude
        )

        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
        Intents.intended(IntentMatchers.hasData(Uri.parse("geo:$latitude,$longitude")))

        Intents.intended(IntentMatchers.hasPackage("com.google.android.apps.maps"))
    }
}