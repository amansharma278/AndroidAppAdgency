package com.example.myads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myads.ui.theme.MyAdsTheme

@Composable
fun DeviceSettingsScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Device ID: 12345", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Location: My Location", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Storage Usage: 50%", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Sync Now")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Clear Cache")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Restart Device")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceSettingsScreenPreview() {
    MyAdsTheme {
        DeviceSettingsScreen()
    }
}