package com.example.safetytag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.safetytag.Navigation.Screen
import com.example.safetytag.Navigation.SetUpNavGraph
import com.example.safetytag.ViewModels.SafetyTagViewModel
import com.example.safetytag.ui.theme.SafetyTagTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: SafetyTagViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafetyTagTheme {

                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SetUpNavGraph(navController = navController, this, this)
                }


            }


        }
    }


}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SafetyTagTheme {
//        SignUpScreen(navController = rememberNavController(),)
//    }
//}