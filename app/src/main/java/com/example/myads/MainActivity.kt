package com.example.myads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myads.ui.theme.MyAdsTheme

sealed class Screen {
    object Login : Screen()
    data class ActivationSuccess(val deviceId: String) : Screen()
    object Home : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAdsTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Login -> {
                            LoginScreen(onLoginSuccess = { deviceId ->
                                currentScreen = Screen.ActivationSuccess(deviceId)
                            })
                        }
                        is Screen.ActivationSuccess -> {
                            ActivationSuccessScreen(
                                deviceName = "Display-${screen.deviceId}",
                                location = "Main Lobby",
                                onStartSyncing = {
                                    currentScreen = Screen.Home
                                }
                            )
                        }
                        is Screen.Home -> {
                            VideoListScreen()
                        }
                    }
                }
            }
        }
    }
}
