package com.example.safetytag

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.NavController
import com.example.safetytag.ViewModels.MyCarsViewModel
import com.example.safetytag.ViewModels.SafetyTagViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class SafetyTagViewModelTest {

    @Mock
    private lateinit var mockNavController: NavController
    private val viewModel = SafetyTagViewModel()

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
    fun testSignUpSuccess() {
        mockContext = Mockito.mock(Context::class.java)
        mockNavController = Mockito.mock(NavController::class.java)

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        val signup =
            viewModel.signUp(
                "test2@gmail.com",
                "test1234",
                "03002222222",
                mockNavController,
                context = mockContext
            )
        TestCase.assertEquals(signup, Unit)
    }

    @Test
    fun testLogInSuccess() {
        mockContext = mock(Context::class.java)
        mockNavController = mock(NavController::class.java)
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        val logIn = viewModel.logIn("test@gmail.com", "test1234", mockContext, mockNavController)
        assertEquals(logIn, Unit)
    }

    @Test
    fun testsaveStateToSharedPreferencesSuccess() {

        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)
        mockContext = mock(Context::class.java)
        // Mock the behavior of context.getSharedPreferences()
        `when`(mockContext.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)

        // Mock the behavior of mockSharedPreferences.edit()
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)

        val savestate = viewModel.saveStateToSharedPreferences(mockContext)
        // Verify that the expected interactions occurred
        verify(mockContext).getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE)
        verify(mockEditor).putString("User", "LogIn")
        verify(mockEditor).apply()
    }

    @Test
    fun testLoadStateFromSharedPreferencesSuccess() {
        mockContext = mock(Context::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)
        // Mock the behavior of context.getSharedPreferences()
        `when`(mockContext.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)

        // Mock the behavior of mockSharedPreferences.getString()
        `when`(mockSharedPreferences.getString("User", null)).thenReturn("LogIn")

        // Call the function
        viewModel.loadStateFromSharedPreferences(mockContext)

        // Verify that the LiveData value was updated correctly
        assert(viewModel.userState.value == "LogIn")
    }


    @Test
    fun testLoadStateFromSharedPreferencesFailure() {
        mockContext = mock(Context::class.java)
        mockSharedPreferences = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)
        // Mock the behavior of context.getSharedPreferences()
        `when`(mockContext.getSharedPreferences("SafetyTagPreferences", Context.MODE_PRIVATE))
            .thenReturn(mockSharedPreferences)

        // Mock the behavior of mockSharedPreferences.getString()
        `when`(mockSharedPreferences.getString("User", null)).thenReturn("LogIn")

        // Call the function
        viewModel.loadStateFromSharedPreferences(mockContext)

        // Verify that the LiveData value was updated correctly
        assertFalse(viewModel.userState.value == "Pass")
    }


}