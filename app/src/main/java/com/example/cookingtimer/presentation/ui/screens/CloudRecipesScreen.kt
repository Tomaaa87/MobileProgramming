package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cookingtimer.model.data.remote.CloudRecipe
import com.example.cookingtimer.presentation.view_model.cloud.CloudRecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudRecipesScreen(
    viewModel: CloudRecipeViewModel = hiltViewModel()
) {
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedRecipe by remember { mutableStateOf<CloudRecipe?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cloud Recipes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedRecipe = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.loadRecipes() }) {
                        Text("Retry")
                    }
                }
            } else if (recipes.isEmpty()) {
                Text(
                    text = "No cloud recipes found.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(recipes) { recipe ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            ListItem(
                                headlineContent = { Text(recipe.title) },
                                supportingContent = { Text(recipe.description) },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = {
                                            selectedRecipe = recipe
                                            showDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                        IconButton(onClick = { recipe.id?.let { viewModel.deleteRecipe(it) } }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            RecipeDialog(
                recipe = selectedRecipe,
                onDismiss = { showDialog = false },
                onConfirm = { title, description ->
                    if (selectedRecipe == null) {
                        viewModel.addRecipe(CloudRecipe(title = title, description = description))
                    } else {
                        viewModel.updateRecipe(
                            selectedRecipe!!.id!!,
                            CloudRecipe(id = selectedRecipe!!.id, title = title, description = description)
                        )
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun RecipeDialog(
    recipe: CloudRecipe?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(recipe?.title ?: "") }
    var description by remember { mutableStateOf(recipe?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (recipe == null) "Add Recipe" else "Edit Recipe") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, description) },
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
