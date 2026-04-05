package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.TimerGrid
import com.example.cookingtimer.presentation.ui.components.TimerItem

@Composable
fun DashboardScreen() {
    val categories = listOf("All", "Breakfast", "Lunch", "Dinner", "Dessert", "Quick Snacking")
    
    val activeTimers = listOf(
        TimerItem("Pasta", "10:00", "Boil water, add pasta."),
        TimerItem("Eggs", "07:00", "Soft boiled."),
        TimerItem("Rice", "18:00", "Simmer, lid on."),
        TimerItem("Cookies", "12:00", "Bake and watch."),
        TimerItem("Steak", "06:00", "Medium rare."),
        TimerItem("Tea", "03:00", "English breakfast.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ScreenHeader(
                title = "Cooking Timer",
                subtitle = "Hello, Tom! Ready to cook something delicious?"
            )
        }

        item {
            Text(
                text = "Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    CategoryChip(category)
                }
            }
        }

        item {
            Text(
                text = "Active Timers",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TimerGrid(
                timers = activeTimers,
                modifier = Modifier.height(450.dp)
            )
        }
    }
}

@Composable
private fun CategoryChip(name: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}