package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.TimerCard

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Cooking Timer",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hello, Tom!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Active Timers",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        CenteredTimer(
            title = "Pasta",
            duration = "10:00",
            description = "Boil water, add pasta."
        )

        Spacer(modifier = Modifier.height(14.dp))

        CenteredTimer(
            title = "Eggs",
            duration = "07:00",
            description = "Soft boiled."
        )

        Spacer(modifier = Modifier.height(14.dp))

        CenteredTimer(
            title = "Rice",
            duration = "18:00",
            description = "Simmer, lid on."
        )

        Spacer(modifier = Modifier.height(14.dp))

        CenteredTimer(
            title = "Cookies",
            duration = "12:00",
            description = "Bake and watch."
        )
    }
}

@Composable
private fun CenteredTimer(title: String, duration: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TimerCard(title = title, duration = duration, description = description)
    }
}