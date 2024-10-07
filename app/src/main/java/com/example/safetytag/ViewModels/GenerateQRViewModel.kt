package com.example.safetytag.ViewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetytag.Model.Car
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class GenerateQRViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> = _qrCodeBitmap

    private var downloadImagesUri = mutableStateOf<Uri?>(null)

    private val _state = MutableStateFlow(null)

    //    private val _stateFlow by mutableStateOf("hello")
    val state = _state.asStateFlow()

    var loading by mutableStateOf(false)
        private set
    fun generateQrCode(
        carName: String,
        carColor: String,
        regNo: String,
        phoneNo: String,
        context: Context
    ) {

        val iscarNameValid = carName.isEmpty()
        val iscarColorEmpty = carColor.isEmpty()
        val isregNoEmpty = regNo.isEmpty()
        val isphoneNoEmpty = phoneNo.isEmpty()

        when {
            iscarNameValid -> {
                Toast.makeText(context, "Car Name cannot be empty", Toast.LENGTH_SHORT).show()
            }

            iscarColorEmpty -> {
                Toast.makeText(context, "Car Color cannot be empty", Toast.LENGTH_SHORT).show()
            }

            isregNoEmpty -> {
                Toast.makeText(context, "Reg No cannot be empty", Toast.LENGTH_SHORT).show()
            }

            isphoneNoEmpty -> {
                Toast.makeText(context, "Phone no cannot be empty", Toast.LENGTH_SHORT).show()
            }

            else -> {

                loading=true
                auth = FirebaseAuth.getInstance()
                val uid = auth.currentUser?.uid.toString()
                val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                val car = Car(uid, carName, carColor, regNo, phoneNo, null)


                //Generate QR COde
                var qrgEncoder = QRGEncoder(car.toString(), QRGContents.Type.TEXT, 1500)
                qrgEncoder.setColorBlack(Color.BLACK)
                qrgEncoder.setColorWhite(Color.WHITE)

                viewModelScope.launch {
                    try {

                        // Getting QR-Code as Bitmap
                        val bitmap = qrgEncoder.getBitmap()
                        Log.d("called", "generateqr $bitmap")
                        val uri = uploadQRCodeToFirebaseStorage(bitmap)
                        Log.d("uriretrived", "after function called: ${uri}")

                        val uploadcardata = Car(uid, carName, carColor, regNo, phoneNo, uri)

                        val pushRef = databaseReference.child(uid).push()
                        pushRef.setValue(uploadcardata).addOnCompleteListener {
                            if (it.isSuccessful) {
                                loading=false
                                Toast.makeText(context, "Save this QR Code", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context,  "Failed generating QR Code",Toast.LENGTH_SHORT).show()
                            }
                        }
                        // Setting Bitmap to ImageView
                        updateQRCodeBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.v(TAG, e.toString())
                    }
                }
            }
        }


    }

    suspend fun uploadQRCodeToFirebaseStorage(bitmap: Bitmap)
            : String
    ? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        val uid = auth.currentUser?.uid
        val storageReference =
            FirebaseStorage.getInstance()
                .getReference("qr_codes/${uid}/${UUID.randomUUID()}/qr_code_image.png")
        val uploadTask = storageReference.putBytes(data)

        return try {
            withContext(Dispatchers.IO) {
                uploadTask.await() // Suspend until the upload task is complete

                // Retrieve the download URL after the upload task is complete
                val downloadUri = storageReference.downloadUrl.await()
                downloadUri.toString() // Return the download URL as a String
            }
        } catch (e: Exception) {
            Log.e("upload", "failed", e)
            null // Return null in case of failure
        }
    }

    fun updateQRCodeBitmap(bitmap: Bitmap) {
        Log.d("called", "called")
        _qrCodeBitmap.value = bitmap
    }

}