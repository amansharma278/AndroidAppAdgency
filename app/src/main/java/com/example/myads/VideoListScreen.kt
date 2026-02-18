package com.example.myads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myads.ui.theme.MyAdsTheme

data class Ad(val id: Int, val title: String, val duration: String, val remainingPlays: Int, val schedule: String, val status: String, val videoUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(onAdClick: (Ad) -> Unit = {}) {
    val ads = listOf(
        Ad(1, "Summer Sale", "30s", 10, "10:00 - 12:00", "Active", "https://www.w3schools.com/tags/mov_bbb.mp4"),
        Ad(2, "New Product Launch", "60s", 5, "14:00 - 15:00", "Scheduled", "https://www.w3schools.com/tags/mov_bbb.mp4"),
        Ad(3, "Holiday Greetings", "15s", 100, "08:00 - 20:00", "Expired", "https://www.w3schools.com/tags/mov_bbb.mp4")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ad Campaign Schedule") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync Status", tint = Color.Green)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Online", style = MaterialTheme.typography.labelMedium)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(ads) { ad ->
                AdCard(ad, onAdClick = onAdClick)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdCard(ad: Ad, onAdClick: (Ad) -> Unit) {
    Card(
        onClick = { onAdClick(ad) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ){
                Text("Thumb", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(ad.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Duration: ${ad.duration}", style = MaterialTheme.typography.bodyMedium)
                Text("Plays Left: ${ad.remainingPlays}", style = MaterialTheme.typography.bodyMedium)
                Text("Schedule: ${ad.schedule}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.width(16.dp))

            val statusColor = when (ad.status) {
                "Active" -> Color(0xFF4CAF50)
                "Scheduled" -> Color(0xFF2196F3)
                else -> Color(0xFFF44336)
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = statusColor.copy(alpha = 0.2f)
            ) {
                Text(
                    ad.status,
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoListScreenPreview() {
    MyAdsTheme {
        VideoListScreen()
    }
}
