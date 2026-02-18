package com.example.myads

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.OutlinedTextField
import androidx.tv.material3.Text
import com.example.myads.ui.theme.MyAdsTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LoginScreen() {
    var deviceId by remember { mutableStateOf("") }
    var secretKey by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App logo
        Text(text = "App Logo", modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = deviceId,
            onValueChange = { deviceId = it },
            label = { Text("Device ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = secretKey,
            onValueChange = { secretKey = it },
            label = { Text("Secret Key") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { /* TODO: Handle login */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* TODO: Handle QR code scan */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Scan QR Code")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Activation Status: Not Activated")
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyAdsTheme {
        LoginScreen()
    }
}