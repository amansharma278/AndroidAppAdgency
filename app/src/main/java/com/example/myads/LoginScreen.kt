package com.example.myads

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myads.ui.theme.MyAdsTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var deviceId by remember { mutableStateOf("") }
    var secretKey by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val deviceDetailsManager = remember { DeviceDetailsManager(context) }
    val apiService = remember { NetworkModule.provideApiService(context) }

    LaunchedEffect(Unit) {
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {
            onLoginSuccess()
        } else {
            isLoading = false
        }
    }

    if (!isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DIGITAL ADS",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Device Activation",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = deviceId,
                onValueChange = { deviceId = it },
                label = { Text("Device ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = secretKey,
                onValueChange = { secretKey = it },
                label = { Text("Secret Key") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        try {
                            val response = apiService.login(mapOf("username" to deviceId, "password" to secretKey))
                            if (response.isSuccessful && response.body() != null) {
                                val tokenResponse = response.body()!!
                                tokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)

                                val deviceDetailsResponse = apiService.getDeviceDetails()
                                if (deviceDetailsResponse.isSuccessful && deviceDetailsResponse.body() != null) {
                                    deviceDetailsManager.saveDeviceDetails(deviceDetailsResponse.body()!!)
                                    onLoginSuccess()
                                } else {
                                    errorMessage = "Failed to fetch device details"
                                }
                            } else {
                                errorMessage = "Invalid credentials"
                            }
                        } catch (e: Exception) {
                            errorMessage = "An error occurred: ${e.message}"
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Activate Device", fontSize = 18.sp)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyAdsTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
