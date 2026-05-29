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
    viewModel: AddTimerViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    // State for Ingredients and Steps
    var ingredientName by remember { mutableStateOf("") }
    var ingredientAmount by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<Pair<String, String>>() }

    var stepInstruction by remember { mutableStateOf("") }
    val steps = remember { mutableStateListOf<String>() }
    
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AddTimerNavigationEvent.NavigateBack -> onNavigateBack()
                is AddTimerNavigationEvent.NavigateToDetail -> onNavigateToDetail(event.timerId)
            }
        }
    }

    when (val state = uiState) {
        is AddTimerUiState.Loading -> LoadingScreen()
        is AddTimerUiState.Error -> ErrorScreen(
            message = state.message,
            onRetry = { viewModel.resetUiState() }
        )
        is AddTimerUiState.Init -> {
            var selectedCategory by rememberSaveable { 
                mutableStateOf(if (state.categories.isNotEmpty()) state.categories[0] else "General") 
            }
            
            StatelessAddTimerScreen(
                name = name,
                onNameChange = { name = it },
                duration = duration,
                onDurationChange = { duration = it },
                isFavorite = isFavorite,
                onFavoriteChange = { isFavorite = it },
                categories = state.categories.ifEmpty { listOf("General") },
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                ingredientName = ingredientName,
                onIngredientNameChange = { ingredientName = it },
                ingredientAmount = ingredientAmount,
                onIngredientAmountChange = { ingredientAmount = it },
                ingredients = ingredients,
                onAddIngredient = {
                    if (ingredientName.isNotBlank() && ingredientAmount.isNotBlank()) {
                        ingredients.add(ingredientName to ingredientAmount)
                        ingredientName = ""
                        ingredientAmount = ""
                    }
                },
                stepInstruction = stepInstruction,
                onStepInstructionChange = { stepInstruction = it },
                steps = steps,
                onAddStep = {
                    if (stepInstruction.isNotBlank()) {
                        steps.add(stepInstruction)
                        stepInstruction = ""
                    }
                },
                onAddTimerClick = {
                    viewModel.addTimer(name, duration, selectedCategory, isFavorite, ingredients.toList(), steps.toList())
                }
            )
        }
        else -> {}
    }
}

@Composable
fun StatelessAddTimerScreen(
    name: String,
    onNameChange: (String) -> Unit,
    duration: String,
    onDurationChange: (String) -> Unit,
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    ingredientName: String,
    onIngredientNameChange: (String) -> Unit,
    ingredientAmount: String,
    onIngredientAmountChange: (String) -> Unit,
    ingredients: List<Pair<String, String>>,
    onAddIngredient: () -> Unit,
    stepInstruction: String,
    onStepInstructionChange: (String) -> Unit,
    steps: List<String>,
    onAddStep: () -> Unit,
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
            subtitle = "Create a new cooking timer with recipe."
        )

        FormCard {
            Text("Basic Information", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isFavorite,
                    onCheckedChange = onFavoriteChange
                )
                Text("Mark as Favorite")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = ingredientName, onValueChange = onIngredientNameChange, label = { Text("Name") })
                    OutlinedTextField(value = ingredientAmount, onValueChange = onIngredientAmountChange, label = { Text("Amount") })
                }
                IconButton(onClick = onAddIngredient) {
                    Text("+")
                }
            }
            ingredients.forEach { (n, a) ->
                Text("• $n ($a)", style = MaterialTheme.typography.bodySmall)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Instructions", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = stepInstruction,
                    onValueChange = onStepInstructionChange,
                    label = { Text("Step Instruction") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onAddStep) {
                    Text("+")
                }
            }
            steps.forEachIndexed { index, s ->
                Text("${index + 1}. $s", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddTimerClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save and View Details")
            }
        }
    }
}
