package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cookingtimer.presentation.ui.components.CircularTimer
import com.example.cookingtimer.presentation.ui.components.ErrorScreen
import com.example.cookingtimer.presentation.ui.components.IngredientList
import com.example.cookingtimer.presentation.ui.components.LoadingScreen
import com.example.cookingtimer.presentation.ui.components.RecipeStepList
import com.example.cookingtimer.presentation.view_model.timer.TimerDetailUiState
import com.example.cookingtimer.presentation.view_model.timer.TimerDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailScreen(
    timerId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TimerDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(timerId) {
        viewModel.loadTimerDetail(timerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timer Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState) {
                is TimerDetailUiState.Init, is TimerDetailUiState.Loading -> {
                    LoadingScreen()
                }
                is TimerDetailUiState.Success -> {
                    TimerDetailContent(
                        state = state,
                        onToggleTimer = { viewModel.toggleTimer(timerId) }
                    )
                }
                is TimerDetailUiState.Error -> {
                    ErrorScreen(message = state.message, onRetry = { viewModel.loadTimerDetail(timerId) })
                }
            }
        }
    }
}

@Composable
fun TimerDetailContent(
    state: TimerDetailUiState.Success,
    onToggleTimer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = state.timer.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularTimer(
                progress = state.timer.progress,
                text = state.timer.durationText,
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.timer.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onToggleTimer,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.timer.isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                if (state.timer.isRunning) Icons.Default.PlayArrow else Icons.Default.PlayArrow, // Replace with Pause if available
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (state.timer.isRunning) "STOP TIMER" else "START TIMER")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

        if (state.ingredients.isNotEmpty()) {
            IngredientList(ingredients = state.ingredients)
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (state.steps.isNotEmpty()) {
            RecipeStepList(steps = state.steps)
        }
    }
}
