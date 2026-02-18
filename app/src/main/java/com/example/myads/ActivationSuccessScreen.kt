package com.example.myads

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myads.ui.theme.MyAdsTheme

@Composable
fun ActivationSuccessScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "success_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Success",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .padding(bottom = 32.dp)
        )

        Text(
            text = "Device Activated!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Device Name", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Lobby Display 01", fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Location", style = MaterialTheme.typography.labelLarge)
                    Text(text = "New York Office", fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { /* TODO: Handle start syncing */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Start Syncing", fontSize = 18.sp)
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