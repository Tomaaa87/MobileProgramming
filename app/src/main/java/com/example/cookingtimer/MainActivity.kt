package com.example.cookingtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cookingtimer.presentation.ui.screens.DashboardScreen
import com.example.cookingtimer.ui.theme.CookingtimerTheme
import androidx.compose.material3.Surface
import com.example.cookingtimer.presentation.ui.screens.AddTimerScreen
import com.example.cookingtimer.presentation.ui.screens.LoginScreen
import com.example.cookingtimer.presentation.ui.screens.RegisterScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ){
                    AddTimerScreen(
                    )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CookingtimerTheme {
        Greeting("Android")
    }
}