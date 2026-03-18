package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF7F2))
            .padding(16.dp)
    ) {
        Text(
            text = "Cooking Timer",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hello, Tom!",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Active Timers",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActiveTimerCircle(name = "Pasta", time = "10:00")
            ActiveTimerCircle(name = "Eggs", time = "07:00")
            ActiveTimerCircle(name = "Rice", time = "18:00")
        }
    }
}

@Composable
fun ActiveTimerCircle(name: String, time: String) {
    Card(
        modifier = Modifier.size(110.dp),
        shape = CircleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = time, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}