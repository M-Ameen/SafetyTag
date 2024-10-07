package com.example.safetytag.ViewModels

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.akash.mybarcodescanner.data.repo.MainRepoImpl
import com.akash.mybarcodescanner.presentation.MainScreenState
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.User
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SafetyTagViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private var OTPId: String = ""

    private val _userState = MutableStateFlow<String?>(null)
    val userState: StateFlow<String?> = _userState.asStateFlow()

    private val _state = MutableStateFlow("")
    val state = _state.asStateFlow()

    var signingUp by mutableStateOf(false)
        private set
    var logginIn by mutableStateOf(false)
        private set


    fun signUp(
        email: String,
        password: String,
        phoneNumber: String,
        navController: NavController,
        context: Context
    ) {
        auth = FirebaseAuth.getInstance()
        signingUp = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                _state.value = "Successful"
                signingUp = false
                navController.popBackStack()
                navController.popBackStack()
                saveStateToSharedPreferences(context)
                Toast.makeText(context, "SignUp Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(route = Screen.DashboardScreen.route)
            } else {
                signingUp = false
                _state.value = task.exception?.message.toString()
                Toast.makeText(context, state.value, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun logIn(email: String, password: String, context: Context, navController: NavController) {
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty()
        val isEmailEmpty = email.isEmpty()

        when {
            isEmailEmpty -> {
                Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            }

            !isEmailValid -> {
                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
            }

            !isPasswordValid -> {
                Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            }

            else -> {
                auth = FirebaseAuth.getInstance()
                logginIn = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _state.value = "Successful"
                            logginIn = false
                            saveStateToSharedPreferences(context)
                            Toast.makeText(context, "LogIn Successful", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                            navController.navigate(route = Screen.DashboardScreen.route)
                        } else {
                            logginIn = false
                            _state.value = task.exception?.message.toString()
                            Toast.makeText(context, state.value, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    fun saveStateToSharedPreferences(context: Context) {
        val sharedPref = context.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("User", "LogIn")
            apply()
        }
    }


    fun loadStateFromSharedPreferences(context: Context) {
        val sharedPref = context.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE)
        val user = sharedPref.getString("User", null)
        _userState.value = user
    }


    fun clearSharedPreferences(context: Context, navController: NavController) {
        val sharedPref = context.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("User")
            apply()
        }
        navController.popBackStack()
        navController.navigate(Screen.LogInScreen.route)
    }

    fun verifyOtp(otp: String) {
        Log.d("codesent", "$OTPId $otp")
        val credential = PhoneAuthProvider.getCredential(OTPId, otp)

    }

    fun uploaduserdata(userEmail: String, phoneNumber: String) {
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user = User(uid, userEmail, phoneNumber)

        val pushRef = databaseReference.child(uid)

        pushRef.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("successful", "untag this carUpload Successful")

            } else {
                Log.d("Failure", "Upload Failed")
            }
        }
    }


    private fun defaultinitiatotp(phoneNumber: String, activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    verificationId: String, token: PhoneAuthProvider.ForceResendingToken
                ) {
                    OTPId = verificationId
                    val credential = PhoneAuthProvider.getCredential(OTPId, "333333")
                    var smscode = credential
//                    signInWithPhoneAuthCredential(credential)
                    Log.d("codesent", "CodeSentcalled: $verificationId")
                    Log.d("codesent", "smscode: $smscode")
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("codesent", "verificatiopn_completed")

                    OTPId = credential.smsCode.toString()
                    Log.d("codesent", "onVerificationCompleted: $OTPId")

//                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        Log.d("error", e.errorCode)

                    } else if (e is FirebaseTooManyRequestsException) {
                        Log.d("error", e.toString())
                    }

                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                }
            }
        }
    }

    fun resetState() {
        _state.update { "" }
    }



}