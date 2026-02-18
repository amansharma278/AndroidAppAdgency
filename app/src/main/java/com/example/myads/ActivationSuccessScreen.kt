package com.example.myads

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.example.myads.ui.theme.MyAdsTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ActivationSuccessScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Add success check animation
        Text(text = "✓", modifier = Modifier.padding(bottom = 32.dp))

        Text(text = "Device Name: My Device")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Location: My Location")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { /* TODO: Handle start syncing */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Start Syncing")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivationSuccessScreenPreview() {
    MyAdsTheme {
        ActivationSuccessScreen()
    }
}