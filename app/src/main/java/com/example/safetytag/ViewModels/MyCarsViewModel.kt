package com.example.safetytag.ViewModels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.safetytag.Model.Car
import com.example.safetytag.Model.TagCar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyCarsViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    val phoneNumberState = mutableStateOf("")

    // State to hold the userId
    val userIdState = mutableStateOf("")

    // State to hold the car name
    val carNameState = mutableStateOf("")

    var carTagLocation = mutableStateOf("")

    // State to hold the car color
    val carColorState = mutableStateOf("")


    // State to hold the registration number
    val carRegNoState = mutableStateOf("")
    var tagCar by mutableStateOf(false)
        private set
    var car by mutableStateOf<Car?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    var loadingTaggedCars by mutableStateOf(false)
        private set

    fun addCar(newcar: Car) {
        car = newcar

    }

    var dataList = mutableStateListOf<Car>()
        private set

    var taggedcardataList = mutableStateListOf<TagCar>()
        private set

    // Function to fetch data from Firebase and update the dataList
    fun fetchDataFromFirebase() {
        loading = true
        // Simulate data fetching from Firebase
        auth = FirebaseAuth.getInstance()
        val current_user_uid = auth.currentUser?.uid.toString()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(current_user_uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (User in snapshot.children) {
                    User.getValue(Car::class.java)?.let {
                        dataList.add(it)
                        Log.d("datalist", dataList.toString())
                        loading = false

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun deleteTaggedCarByChildId(userId: String, regNo: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()
        val query = databaseReference.child("Tags").child(userId.toString())
            .orderByChild("regNo").equalTo(regNo.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (allTags in snapshot.children) {
                    allTags.getValue(TagCar::class.java)?.let { tag ->
                        // Delete the data from the Firebase Realtime Database
                        allTags.ref.removeValue().addOnSuccessListener {
                            Log.d("successful", "Delete Successful")
                        }.addOnFailureListener {
                            Log.d("failure", "Failed to delete data: ${it.message}")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }
        })

    }

    fun fetchDataTaggedcarsFromFirebase() {
        loading=true
        // Simulate data fetching from Firebase
        auth = FirebaseAuth.getInstance()
        val current_user_uid = auth.currentUser?.uid.toString()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Tags").child(current_user_uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taggedcardataList.clear()
                for (User in snapshot.children) {
                    User.getValue(TagCar::class.java)?.let {
                        taggedcardataList.add(it)
                        Log.d("datalist", taggedcardataList.toString())
                        loading=false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }

        })
    }

    fun getCurrentLocation(context: Context) {

    }

    fun createTag(
        context: Context,
        carName: String,
        carColor: String,
        regNo: String,
        phoneNo: String,
        QRCodeImgLink: String,
    ) {
        Log.d("currentlocation","method run")

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Tags")

        val pushRef = databaseReference.child(uid).push()

        var fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            Log.d("currentlocation","checking permission")
        }
        task.addOnSuccessListener {
            if (it != null) {
                carTagLocation.value = "${it.latitude},${it.longitude}"
                val car = TagCar(
                    uid,
                    carName,
                    carColor,
                    regNo,
                    phoneNo,
                    QRCodeImgLink,
                    carTagLocation.value
                )
                Log.d("currentlocation","success")

                pushRef.setValue(car).addOnCompleteListener {
                    if (it.isSuccessful) {
                        tagCar = true
                    } else {
                        tagCar = false
                    }
                }
            }
        }.addOnFailureListener {
            Log.d("currentlocation", "${it.localizedMessage}")
        }


    }

    fun openGoogleMaps(context: Context, latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)

    }
}



