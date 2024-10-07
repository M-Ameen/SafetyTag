package com.example.safetytag

import android.content.Context
import androidx.navigation.NavController
import com.example.safetytag.ViewModels.SafetyTagViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Mock
    private lateinit var mockContext: Context
    @Mock
    private lateinit var mockNavController: NavController

    private val viewModel = SafetyTagViewModel()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }



}