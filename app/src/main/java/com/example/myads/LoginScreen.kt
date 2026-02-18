package com.example.myads

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myads.ui.theme.MyAdsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var deviceId by remember { mutableStateOf("") }
    var secretKey by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App logo / Title
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
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = secretKey,
            onValueChange = { secretKey = it },
            label = { Text("Secret Key") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    // Simulate Dummy API Call
                    delay(2000)
                    
                    if (deviceId.isNotBlank() && secretKey == "1234") {
                        // Success Logic
                        onLoginSuccess(deviceId)
                    } else {
                        // Unauthenticated Logic
                        errorMessage = "Invalid Device ID or Secret Key"
                    }
                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Activate Device", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* TODO: Handle QR code scan */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading
        ) {
            Text("Scan QR Code", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = if (isLoading) "Checking activation..." else "Status: Not Activated",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
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
