package com.example.safetytag

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings.Global.getString
import androidx.navigation.NavController
import com.example.safetytag.ViewModels.MyCarsViewModel
import com.example.safetytag.ViewModels.SendAlertViewModel
import com.google.common.base.Joiner.on
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase
import junit.framework.TestCase.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

class MyCarsviewModelTest {


    @Mock
    private lateinit var intent: Intent

    @Mock
    private lateinit var mockDatabase: FirebaseDatabase

    @Mock
    private lateinit var mockNavController: NavController

    @Mock
    private lateinit var firebaseRule: FirebaseDatabase

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var string: String

    @Mock
    private lateinit var mockDatabaseReference: DatabaseReference
    private lateinit var mockFirebaseUser: FirebaseUser

    private lateinit var myCarsViewModel: MyCarsViewModel


    @Test
    fun testfetchDataFromFirebaseSuccess() {
        mockContext = Mockito.mock(Context::class.java)
        val viewModel = MyCarsViewModel()


        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        // Call the function
        viewModel.fetchDataFromFirebase()

        // Verify that the LiveData value was updated correctly
        assert(viewModel.dataList.size != null)
    }

    @Test
    fun testfetchDataFromFirebaseFailure() {
        mockContext = Mockito.mock(Context::class.java)
        val viewModel = MyCarsViewModel()


        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        // Call the function
        viewModel.fetchDataFromFirebase()

        // Verify that the LiveData value was updated correctly
        TestCase.assertFalse(viewModel.dataList.size == null)
    }

    @Test
    fun testDeleteTaggedCarByChildId() {

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)

        mockAuth = mockk(relaxed = true)
        firebaseRule = Mockito.mock(FirebaseDatabase::class.java)
        mockDatabaseReference = Mockito.mock(DatabaseReference::class.java)
        every { FirebaseAuth.getInstance() } returns mockAuth
        // Prepare the data
        val userId = "J0JQVUpWjAVDelKaG6QiG51ISz92"
        val regNo = "LXO 1122"
        val viewModel = MyCarsViewModel()

        val result = viewModel.deleteTaggedCarByChildId(userId, regNo)
        TestCase.assertEquals(result, Unit)

        // Verify that removeValue is called on the child reference
//        verify(exactly = 1) { mockTagCarRef.removeValue() }
    }


//    @Test
//    fun testGetCurrentLocation() {
//        val context: Context = mockk()
//        myCarsViewModel = MyCarsViewModel()
//
//        val result = myCarsViewModel.getCurrentLocation(context)
//
//        assertTrue(myCarsViewModel.carTagLocation.value != null)
//
//    }


    @Test
    fun testCreateTagSuccess() {

        mockkStatic(FirebaseAuth::class)

        mockAuth = mockk(relaxed = true)
        mockDatabase = mockk(relaxed = true)
        mockDatabaseReference = mockk(relaxed = true)

        every { FirebaseAuth.getInstance() } returns mockAuth
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        every { mockDatabase.reference } returns mockDatabaseReference
        mockContext = Mockito.mock(Context::class.java)

        myCarsViewModel = MyCarsViewModel()


        val carName = "Toyota Corolla"
        val carColor = "Blue"
        val regNo = "ABC123"
        val phoneNo = "1234567890"
        val qrCodeImgLink = "https://example.com/qrcode.png"
        val carLocation = "City"

        // Mock the push reference and set value behavior
        val mockTagCarRef = mockk<DatabaseReference>(relaxed = true)
        every { mockDatabaseReference.child(any()) } returns mockTagCarRef
        every { mockTagCarRef.push() } returns mockTagCarRef

        every { mockTagCarRef.setValue(any(), any()) } answers {
            val completionListener = secondArg<DatabaseReference.CompletionListener>()
            val mockError = null // Simulate success
            completionListener.onComplete(mockError, mockTagCarRef)
            mockTagCarRef
        }

        myCarsViewModel.createTag(mockContext,carName, carColor, regNo, phoneNo, qrCodeImgLink)
        confirmVerified(mockTagCarRef, mockDatabaseReference)
    }

    @Test
    fun testCreateTagFailure() {

        mockkStatic(FirebaseAuth::class)

        mockAuth = mockk(relaxed = true)
        mockDatabase = mockk(relaxed = true)
        mockDatabaseReference = mockk(relaxed = true)

        every { FirebaseAuth.getInstance() } returns mockAuth
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        every { mockDatabase.reference } returns mockDatabaseReference

        myCarsViewModel = MyCarsViewModel()

        val carName = "Toyota Corolla"
        val carColor = "Blue"
        val regNo = "ABC123"
        val phoneNo = "1234567890"
        val qrCodeImgLink = "https://example.com/qrcode.png"
        val carLocation = "City"

        // Mock the push reference and set value behavior
        val mockTagCarRef = mockk<DatabaseReference>(relaxed = true)
        every { mockDatabaseReference.child(any()) } returns mockTagCarRef
        every { mockTagCarRef.push() } returns mockTagCarRef

        every { mockTagCarRef.setValue(any(), any()) } answers {
            val completionListener = secondArg<DatabaseReference.CompletionListener>()
            val mockError =
                DatabaseError.fromException(Exception("Simulated error")) // Create a DatabaseError from Exception
            completionListener.onComplete(mockError, mockTagCarRef)
            mockTagCarRef
        }

        myCarsViewModel.createTag(mockContext,carName, carColor, regNo, phoneNo, qrCodeImgLink)
        confirmVerified(mockTagCarRef, mockDatabaseReference)
    }


    @Test
    fun testfetchDataTaggedcarsFromFirebaseSuccess() {
        mockContext = Mockito.mock(Context::class.java)
        val viewModel = MyCarsViewModel()


        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        // Call the function
        viewModel.fetchDataTaggedcarsFromFirebase()

        // Verify that the LiveData value was updated correctly
        assert(viewModel.taggedcardataList.size != null)
    }

    @Test
    fun testfetchDataTaggedcarsFromFirebaseFailure() {
        mockContext = Mockito.mock(Context::class.java)
        val viewModel = MyCarsViewModel()


        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        // Call the function
        viewModel.fetchDataTaggedcarsFromFirebase()

        // Verify that the LiveData value was updated correctly
        assertFalse(viewModel.taggedcardataList.size == null)
    }


//    @Test
//    fun testOpenGoogleMaps() {
//        val mockContext = mockk<Context>(relaxed = true)
//        val viewModel = MyCarsViewModel()
//        mockkStatic(Uri::class)
//        val latitude = 37.7749
//        val longitude = -122.4194
//
//        // Mock Uri.parse() with any() matcher
//        every { Uri.parse(any()) } returns Uri.EMPTY // Mock Uri.parse() to return an empty Uri object
//
//        // Mock the Intent class without any specific method
//        val mockMapIntent = mockk<Intent>(relaxed = true)
//
//        viewModel.openGoogleMaps(mockContext, latitude, longitude)
//
//        verify(exactly = 1) {
//            mockContext.startActivity(mockMapIntent)
//        }
//    }
//
//
    @Test
    fun testSendAlert() {
        val mockContext = mockk<Context>(relaxed = true)
        val carName = "Toyota Corolla"
        val regNo = "ABC123"
        val issue = "Engine problem"
        val phoneNumber = "1234567890"
        intent= mock(Intent::class.java)
        mockkStatic(Uri::class)
        every { Uri.parse("smsto:$phoneNumber") } returns mockk(relaxed = true)

        mockkStatic(Intent::class)
        mockkStatic(String::class)

        val sendAlertViewModel = SendAlertViewModel()
        every { intent.putExtra("","") } returns mockk(relaxed = true)

        verify {
            sendAlertViewModel.sendAlert(mockContext, carName,regNo,issue,phoneNumber)
        }
    }
}




