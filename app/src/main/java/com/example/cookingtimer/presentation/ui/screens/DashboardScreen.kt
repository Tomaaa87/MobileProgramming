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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    onTimerClick: (Int) -> Unit
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
                name = state.userName,
                searchQuery = state.searchQuery,
                selectedCategory = state.selectedCategory,
                timers = state.timers,
                categories = state.categories,
                onSearchChange = viewModel::onSearchChange,
                onCategorySelect = viewModel::onCategorySelect,
                onTimerToggle = viewModel::toggleTimer,
                onDeleteTimer = viewModel::deleteTimer,
                onTimerClick = onTimerClick
            )
        }
        else -> { /* Init */ }
    }
}

@Composable
fun StatelessDashboardScreen(
    name: String,
    searchQuery: String,
    selectedCategory: String,
    timers: List<TimerData>,
    categories: List<String>,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onTimerToggle: (Int) -> Unit,
    onDeleteTimer: (Int) -> Unit,
    onTimerClick: (Int) -> Unit
) {
    var isShortTimersOnly by remember { mutableStateOf(false) }

    val filteredTimers = timers.filter { timer ->
        val matchesSearch = timer.title.contains(searchQuery, ignoreCase = true) || 
                          timer.description.contains(searchQuery, ignoreCase = true)
        
        val matchesCategory = selectedCategory == "All" || 
                             timer.description.contains("Category: $selectedCategory", ignoreCase = true)
        
        val matchesShort = !isShortTimersOnly || (timer.totalSeconds / 60) <= 15
        
        matchesSearch && matchesCategory && matchesShort
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
                title = "Dashboard",
                subtitle = "Hello, $name! Ready to cook something delicious?"
            )
        }

        item(span = { GridItemSpan(2) }) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                label = { Text("Search timers") },
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
                        CategoryChip(
                            name = category,
                            isSelected = category == selectedCategory,
                            onClick = { onCategorySelect(category) }
                        )
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

        // --- Active Timers ---
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Active Timers",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (filteredTimers.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "No timers found for this criteria.",
                    modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filteredTimers) { timer ->
                TimerCardWithAction(
                    timer = timer,
                    onToggle = { onTimerToggle(timer.id) },
                    onDelete = { onDeleteTimer(timer.id) },
                    onClick = { onTimerClick(timer.id) }
                )
            }
        }
    }
}

@Composable
fun TimerCardWithAction(timer: TimerData, onToggle: () -> Unit, onDelete: () -> Unit, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularTimer(
            progress = timer.progress,
            text = timer.durationText,
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = timer.title, 
            fontWeight = FontWeight.Bold, 
            fontSize = 16.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Toggle",
                    tint = if (timer.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
