package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.model.TimerData
import com.example.cookingtimer.presentation.ui.components.ErrorScreen
import com.example.cookingtimer.presentation.ui.components.LoadingScreen
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.view_model.timer.TimerNavigationEvent
import com.example.cookingtimer.presentation.view_model.timer.TimerUiState
import com.example.cookingtimer.presentation.view_model.timer.TimerViewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    onAddTimerClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timers by viewModel.timers.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is TimerNavigationEvent.Navigate -> { /* Handle if needed */ }
                is TimerNavigationEvent.NavigateBack -> { /* Handle if needed */ }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTimerClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Timer")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is TimerUiState.Loading -> LoadingScreen()
                is TimerUiState.Error -> ErrorScreen(
                    message = (uiState as TimerUiState.Error).message,
                    onRetry = { viewModel.resetUiState() }
                )
                else -> {
                    StatelessTimerScreen(
                        timers = timers,
                        onToggle = { id, running -> viewModel.toggleTimer(id, running) },
                        onDelete = { id -> viewModel.deleteTimer(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatelessTimerScreen(
    timers: List<TimerData>,
    onToggle: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "My Timers",
            subtitle = "Manage your active cooking timers."
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(timers) { timer ->
                TimerItemRow(
                    timer = timer,
                    onToggle = { onToggle(timer.id, !timer.isRunning) },
                    onDelete = { onDelete(timer.id) }
                )
            }
        }
    }
}

@Composable
fun TimerItemRow(
    timer: TimerData,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = timer.title, style = MaterialTheme.typography.titleMedium)
                Text(text = timer.duration, style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = onToggle) {
                    Icon(
                        if (timer.isRunning) Icons.Default.PlayArrow else Icons.Default.PlayArrow, // Just using play for now
                        contentDescription = if (timer.isRunning) "Pause" else "Start"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
