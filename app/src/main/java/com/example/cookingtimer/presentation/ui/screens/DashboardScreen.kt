package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.TimerGrid
import com.example.cookingtimer.presentation.ui.components.TimerItem

@Composable
fun DashboardScreen() {
    val activeTimers = listOf(
        TimerItem("Pasta", "10:00", "Boil water, add pasta."),
        TimerItem("Eggs", "07:00", "Soft boiled."),
        TimerItem("Rice", "18:00", "Simmer, lid on."),
        TimerItem("Cookies", "12:00", "Bake and watch.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Cooking Timer",
            subtitle = "Hello, Tom! Ready to cook something delicious?"
        )

        Text(
            text = "Active Timers",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        TimerGrid(timers = activeTimers)
    }
}