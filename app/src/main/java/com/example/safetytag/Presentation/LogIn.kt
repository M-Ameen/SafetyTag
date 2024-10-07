package com.example.safetytag.Presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.R
import com.example.safetytag.ViewModels.SafetyTagViewModel

@Composable
fun LogInScreen(navController: NavController, context: Context) {

    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val viewModel = viewModel<SafetyTagViewModel>()
    val state = viewModel.userState
    val logIn = viewModel.logginIn

    Column(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#D8D8D8")))
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier,
            color= Color(android.graphics.Color.parseColor("#702FDB")),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(50.dp))
        TF(
            label = R.string.email,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            value = emailInput,
            onValueChanged = { emailInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        TF(
            label = R.string.password,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            value = passwordInput,
            onValueChanged = { passwordInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.logIn(emailInput, passwordInput, context, navController)
            }
        ) {
            Text(
                text = stringResource(R.string.submit),
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("Don't have an account? ")
            }
            withStyle(style = SpanStyle(color =  Color(android.graphics.Color.parseColor("#702FDB")))) {
                append("Sign up ")
            }
        },
            modifier = Modifier.clickable {
                navController.navigate(route = Screen.SignUpScreen.route)
            }
        )
        LaunchedEffect(state) {
            viewModel.loadStateFromSharedPreferences(context)
            state.collect { value ->
                if (value == "LogIn") {
                    navController.popBackStack()
                    navController.navigate(route = Screen.DashboardScreen.route)
                } else {

                }
            }
        }
    }

// Display CircularProgressIndicator in a separate Box
    if (logIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }
    }

}



