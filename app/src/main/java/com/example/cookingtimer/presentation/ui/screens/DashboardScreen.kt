package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.presentation.ui.components.*
import com.example.cookingtimer.presentation.view_model.dashboard.DashboardUiState
import com.example.cookingtimer.presentation.view_model.dashboard.DashboardViewModel
import com.example.cookingtimer.model.TimerData

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    name: String = "Tom",
    email: String = "",
    onShortcutClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is DashboardUiState.Loading -> LoadingScreen()
        is DashboardUiState.Error -> ErrorScreen(
            message = (uiState as DashboardUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        is DashboardUiState.Success -> {
            val state = uiState as DashboardUiState.Success
            StatelessDashboardScreen(
                name = name,
                email = email,
                searchQuery = state.searchQuery,
                timers = state.timers,
                categories = state.categories,
                shortcuts = state.shortcuts,
                onSearchChange = viewModel::onSearchChange,
                onTimerToggle = viewModel::toggleTimer,
                onShortcutClick = onShortcutClick
            )
        }
        else -> { /* Init */ }
    }
}

@Composable
fun StatelessDashboardScreen(
    name: String,
    email: String,
    searchQuery: String,
    timers: List<TimerData>,
    categories: List<String>,
    shortcuts: List<String>,
    onSearchChange: (String) -> Unit,
    onTimerToggle: (Int) -> Unit,
    onShortcutClick: (String) -> Unit
) {
    var isShortTimersOnly by remember { mutableStateOf(false) }

    val filteredTimers = if (isShortTimersOnly) {
        timers.filter {
            val minutes = it.duration.substringBefore(":").toIntOrNull() ?: 0
            minutes <= 15
        }
    } else timers

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
                title = "Dashboard",
                subtitle = "Hello, $name! Ready to cook something delicious?"
            )
        }

        // --- NEW: Shortcut Section (Simplified HomeShortcut) ---
        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Quick Shortcuts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(shortcuts) { shortcut ->
                        Card(
                            modifier = Modifier.clickable { onShortcutClick(shortcut) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Text(shortcut, modifier = Modifier.padding(12.dp), fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = { Text("Search categories") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        CategoryChip(category)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isShortTimersOnly,
                        onCheckedChange = { isShortTimersOnly = it }
                    )
                    Text("Show only short timers (< 15 min)")
                }
            }
        }

        // --- Active Timers (Simplified TimerScreen elements) ---
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Active Timers",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(filteredTimers) { timer ->
            TimerCardWithAction(
                timer = timer,
                onToggle = { onTimerToggle(timer.id) }
            )
        }
    }
}

@Composable
fun TimerCardWithAction(timer: TimerData, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(timer.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = onToggle) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Toggle",
                        tint = if (timer.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
            Text(timer.duration, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(timer.description, fontSize = 12.sp, lineHeight = 16.sp)
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
