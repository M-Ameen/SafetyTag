package com.example.safetytag

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safetytag.Presentation.AdtagScreen
import com.example.safetytag.Presentation.LogInScreen
import com.example.safetytag.Presentation.MyCarsScreen
import com.example.safetytag.Presentation.SendAlertScreen
import com.example.safetytag.Presentation.SignUpScreen
import com.example.safetytag.ViewModels.MyCarsViewModel
import com.example.safetytag.ViewModels.SafetyTagViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SafetyTagTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: NavController
    private lateinit var viewModel: SafetyTagViewModel
    private lateinit var context: Context

//    @Before
//    fun setup(){
//        composeTestRule.setContent {
//            navController= rememberNavController()
//        }
//    }


    @Test
    fun signupScreen_verifyContent() {
        composeTestRule.setContent {
            navController = rememberNavController()
            SignUpScreen(navController = navController, activity = ComponentActivity())
        }
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.sign_up
            ),
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.submit
            ),
        ).assertIsDisplayed()

    }

    @Test
    fun signUpScreen_UIElementsDisplayed() {
        composeTestRule.setContent {
            navController = rememberNavController()
            SignUpScreen(navController, composeTestRule.activity)
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sign_up))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phonenno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.submit))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Already have an Account?").assertIsDisplayed()

    }

    @Test
    fun signInScreen_UIElementsDisplayed() {
        // Launch the composable
        composeTestRule.setContent {
            navController = rememberNavController()
            LogInScreen(navController, composeTestRule.activity)
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.login))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.submit))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Create a new Account?").assertIsDisplayed()
    }

    @Test
    fun sendAlertScreen_UIElementsDisplayed() {
        val qr_code_result =
            "Car(userId=J0JQVUpWjAVDelKaG6QiG51ISz92, carName=Baleno, carColor=Black, regNo=LOD 1125, phoneNo=03056666666, QRCodeImgLink=null)"
        // Launch the composable
        composeTestRule.setContent {
            navController = rememberNavController()
            SendAlertScreen(navController, qr_code_result, composeTestRule.activity)

        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carname))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carregno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phonenno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carissue))
            .assertIsDisplayed()
        composeTestRule.onAllNodes(hasText(composeTestRule.activity.getString(R.string.send_alert)))
            .assertCountEquals(2)
    }

    @Test
    fun AdtagScreen_UIElementsDisplayed() {
        val qr_code_result =
            "Car(userId=J0JQVUpWjAVDelKaG6QiG51ISz92, carName=Baleno, carColor=Black, regNo=LOD 1125, phoneNo=03056666666, QRCodeImgLink=null)"
        // Launch the composable
        composeTestRule.setContent {
            val viewmodel = MyCarsViewModel()
            navController = rememberNavController()
            AdtagScreen(
                navController, context = composeTestRule.activity, sharedViewModel = viewmodel
            )

        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carname))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carcolor))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carregno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phonenno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_tag))
            .assertIsDisplayed()
        composeTestRule.onAllNodes(hasText(composeTestRule.activity.getString(R.string.carlocation)))
            .assertCountEquals(2)
    }

    @Test
    fun MyCarsScreenScreen_UIElementsDisplayed() {
        val qr_code_result =
            "Car(userId=J0JQVUpWjAVDelKaG6QiG51ISz92, carName=Baleno, carColor=Black, regNo=LOD 1125, phoneNo=03056666666, QRCodeImgLink=null)"
        // Launch the composable
        composeTestRule.setContent {
            val viewmodel = MyCarsViewModel()
            navController = rememberNavController()
            MyCarsScreen(
                navController,  sharedViewModel = viewmodel
            )

        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carname))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carcolor))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.carregno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phonenno))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.create_tag))
            .assertIsDisplayed()
        composeTestRule.onAllNodes(hasText(composeTestRule.activity.getString(R.string.carlocation)))
            .assertCountEquals(2)
    }

    @Test
    fun signUpButton_Click_NavigatetoLogInSuccess() {
        // Launch the composable
        composeTestRule.setContent {
            navController = rememberNavController()
            viewModel = SafetyTagViewModel()
            context= LocalContext.current
            SignUpScreen(navController, composeTestRule.activity)
        }
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .performTextInput("test8@example.com")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.phonenno))
            .performTextInput("1234567890")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
            .performTextInput("password123")
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.submit))
            .performClick()
        viewModel.signUp("ali@gmail.com","Ameen2222", "03033333333", navController, context)
        composeTestRule.waitForIdle()
//        navController.assertCurrentRouteName(Screen.LogInScreen.route)

    }


    fun NavController.assertCurrentRouteName(expectedRouteName: String) {
        Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
    }
}
