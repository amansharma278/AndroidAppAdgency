package com.example.myads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myads.ui.theme.MyAdsTheme

@Composable
fun PlaybackStatusScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Currently Playing: Ad Name", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total Plays Completed: 5", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Remaining Plays: 5", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Next Scheduled Ad: Ad Name 2", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackStatusScreenPreview() {
    MyAdsTheme {
        PlaybackStatusScreen()
    }
}