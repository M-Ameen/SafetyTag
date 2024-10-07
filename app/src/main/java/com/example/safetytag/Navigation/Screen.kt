package com.example.safetytag.Navigation


const val SEND_ALERT_ARGUMENT_KEY = "qr_code_result"

sealed class Screen(val route: String) {
    object SignUpScreen : Screen("signUp_screen")
    object LogInScreen : Screen("logIn_screen")
    object VerifyOTPScreen : Screen("verify_otp_screen")
    object DashboardScreen : Screen("dashboard_screen")
    object CreateQrCode : Screen("create_qr_code_screen")
    object ScanQrCode : Screen("scan_qr_code_screen")
    object MyCarsScreen : Screen("my_cars_screen")
    object SendAlertScreen : Screen("send_alert_screen/{$SEND_ALERT_ARGUMENT_KEY}")
    object TagCarScreen : Screen("tag_car_screen")
    object UnTagCarScreen : Screen("un_tag_car_screen")
    object AdTagScreen : Screen("ad_tag_screen")
}
