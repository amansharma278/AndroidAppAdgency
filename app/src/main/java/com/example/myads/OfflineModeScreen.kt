package com.example.myads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.example.myads.ui.theme.MyAdsTheme

@Composable
fun OfflineModeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Playing downloaded ads")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Internet Status: Offline")
    }
}

@Preview(showBackground = true)
@Composable
fun OfflineModeScreenPreview() {
    MyAdsTheme {
        OfflineModeScreen()
    }
}