package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.presentation.ui.components.*
import com.example.cookingtimer.presentation.view_model.timer.AddTimerNavigationEvent
import com.example.cookingtimer.presentation.view_model.timer.AddTimerUiState
import com.example.cookingtimer.presentation.view_model.timer.AddTimerViewModel

@Composable
fun AddTimerScreen(
    source: String,
    viewModel: AddTimerViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    
    val categories = listOf("General", "Baking", "Grilling", "Boiling", "Steaming")
    var selectedCategory by rememberSaveable { mutableStateOf(categories[0]) }
    
    val difficulties = listOf("Easy", "Medium", "Hard")
    var selectedDifficulty by rememberSaveable { mutableStateOf(difficulties[0]) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AddTimerNavigationEvent.NavigateBack -> onNavigateBack()
                else -> { /* Handle other events if any */ }
            }
        }
    }

    when (uiState) {
        is AddTimerUiState.Loading -> LoadingScreen()
        is AddTimerUiState.Error -> ErrorScreen(
            message = (uiState as AddTimerUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        else -> {
            StatelessAddTimerScreen(
                source = source,
                name = name,
                onNameChange = { name = it },
                duration = duration,
                onDurationChange = { duration = it },
                isFavorite = isFavorite,
                onFavoriteChange = { isFavorite = it },
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                difficulties = difficulties,
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = { selectedDifficulty = it },
                onAddTimerClick = {
                    viewModel.addTimer(name, duration, selectedCategory, selectedDifficulty, isFavorite)
                }
            )
        }
    }
}

@Composable
fun StatelessAddTimerScreen(
    source: String,
    name: String,
    onNameChange: (String) -> Unit,
    duration: String,
    onDurationChange: (String) -> Unit,
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    difficulties: List<String>,
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit,
    onAddTimerClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        ScreenHeader(
            title = "Add Timer",
            subtitle = "Source: $source. Create a new cooking timer."
        )

        FormCard {
            ValidatedTextField(
                value = name,
                onValueChange = onNameChange,
                label = "Timer Name",
                placeholder = "e.g. Pasta"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ValidatedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = "Duration (minutes)",
                placeholder = "e.g. 10"
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownField(
                label = "Category",
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownField(
                label = "Difficulty",
                options = difficulties,
                selectedOption = selectedDifficulty,
                onOptionSelected = onDifficultySelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = onFavoriteChange
                )
                Text("Mark as Favorite")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddTimerClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Timer")
            }
        }
    }
}
