package com.example.safetytag.Navigation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.safetytag.Presentation.AdtagScreen
import com.example.safetytag.Presentation.CreateQRCodeScreen
import com.example.safetytag.Presentation.Dashboard
import com.example.safetytag.Presentation.LogInScreen
import com.example.safetytag.Presentation.MyCarsScreen
import com.example.safetytag.Presentation.ScanQRCodeScreen
import com.example.safetytag.Presentation.SendAlertScreen
import com.example.safetytag.Presentation.SignUpScreen
import com.example.safetytag.Presentation.TagCarScreen
import com.example.safetytag.Presentation.VerifyOTPLayout
import com.example.safetytag.ViewModels.MyCarsViewModel
import com.example.safetytag.Navigation.SEND_ALERT_ARGUMENT_KEY as SEND_ALERT_ARGUMENT_KEY1

@Composable
fun SetUpNavGraph(navController: NavHostController, activity: Activity, context: Context) {
    val sharedViewModel: MyCarsViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.LogInScreen.route) {
        composable(route = Screen.LogInScreen.route) {
            LogInScreen(navController = navController, context)
        }
        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController, activity)
        }
        composable(route = Screen.VerifyOTPScreen.route) {
            VerifyOTPLayout(navController = navController)
        }
        composable(route = Screen.DashboardScreen.route) {
            Dashboard(navController = navController)
        }
        composable(route = Screen.CreateQrCode.route) {
            CreateQRCodeScreen(navController = navController)
        }
        composable(route = Screen.ScanQrCode.route) {
            ScanQRCodeScreen(navController = navController)
        }
        composable(route = Screen.MyCarsScreen.route) {
            MyCarsScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(route = Screen.TagCarScreen.route) {
            val context= LocalContext.current
            TagCarScreen(navController = navController,context)
        }

        composable(route = Screen.AdTagScreen.route) {
            AdtagScreen(navController = navController, sharedViewModel = sharedViewModel, context)
        }
        composable(
            route = Screen.SendAlertScreen.route,
            arguments = listOf(navArgument(SEND_ALERT_ARGUMENT_KEY1) {
                type = NavType.StringType
            }
            )
        ) {
            val context= LocalContext.current
            val qr_code_result = it.arguments?.getString(SEND_ALERT_ARGUMENT_KEY1) ?: ""
            Log.d("qr_code_result", qr_code_result)
            SendAlertScreen(navController = navController, qr_code_result,context)
        }
    }
}