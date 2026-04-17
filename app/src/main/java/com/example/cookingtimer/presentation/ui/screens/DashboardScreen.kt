package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.TimerCard
import com.example.cookingtimer.presentation.ui.components.TimerItem
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DashboardScreen(
    name: String = "Tom",
    email: String = ""
) {
    var text by rememberSaveable { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    val categories = listOf(
        "All", "Breakfast", "Lunch", "Dinner", "Dessert",
        "Snacking", "Healthy", "Vegan", "Italian", "Baking"
    )

    val activeTimers = listOf(
        TimerItem("Pasta", "10:00", "Boil water, add pasta."),
        TimerItem("Eggs", "07:00", "Soft boiled."),
        TimerItem("Rice", "18:00", "Simmer, lid on."),
        TimerItem("Cookies", "12:00", "Bake and watch."),
        TimerItem("Steak", "06:00", "Medium rare."),
        TimerItem("Tea", "03:00", "English breakfast."),
        TimerItem("Soup", "45:00", "Low heat simmer."),
        TimerItem("Pizza", "15:00", "Crispy crust.")
    )

    val filteredCategories by remember {
        derivedStateOf {
            categories.filter { it.contains(text, ignoreCase = true) }
        }
    }

    val filteredTimers by remember {
        derivedStateOf {
            activeTimers.filter { timer ->
                if (!isChecked) true
                else {
                    val minutes = timer.duration.substringBefore(":").toIntOrNull() ?: 0
                    minutes <= 15
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item(span = { GridItemSpan(2) }) {
            ScreenHeader(
                title = "Cooking Timer",
                subtitle = "Hello, $name! ($email) Ready to cook something delicious?"
            )
        }

        item(span = { GridItemSpan(2) }) {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Search categories") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column {

                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (filteredCategories.isEmpty()) {
                    Text(
                        text = "No categories match your search.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredCategories) { category ->
                            CategoryChip(category)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text("Show only short timers (< 15 min)")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isChecked) "Filter ON" else "Filter OFF"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Active Timers",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (filteredTimers.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No timers available.", textAlign = TextAlign.Center)
                }
            }
        } else {
            items(filteredTimers) { timer ->
                TimerCard(
                    title = timer.title,
                    duration = timer.duration,
                    description = timer.description
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(32.dp))
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