package com.example.myads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myads.ui.theme.MyAdsTheme

@Composable
fun AutoPlaybackScreen(onAdminGesture: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Full-screen video player placeholder
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Video Playback Area",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        // Hidden Admin Gesture Area (e.g., top-left corner)
        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopStart)
                .clickable(onClick = onAdminGesture)
        )

        // Optional small bottom progress bar
        LinearProgressIndicator(
            progress = { 0.7f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.Transparent
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1920dp,height=1080dp,navigation=buttons")
@Composable
fun AutoPlaybackScreenPreview() {
    MyAdsTheme {
        AutoPlaybackScreen()
    }
}
