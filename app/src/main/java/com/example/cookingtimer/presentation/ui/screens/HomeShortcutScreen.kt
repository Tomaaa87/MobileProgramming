package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.model.ScreenShortcutData
import com.example.cookingtimer.presentation.ui.components.ErrorScreen
import com.example.cookingtimer.presentation.ui.components.LoadingScreen
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.view_model.home.HomeShortcutNavigationEvent
import com.example.cookingtimer.presentation.view_model.home.HomeShortcutUiState
import com.example.cookingtimer.presentation.view_model.home.HomeShortcutViewModel

@Composable
fun HomeShortcutScreen(
    viewModel: HomeShortcutViewModel,
    onScreenClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is HomeShortcutNavigationEvent.Navigate -> { /* Handle if needed */ }
                is HomeShortcutNavigationEvent.NavigateBack -> { /* Handle if needed */ }
            }
        }
    }

    when (uiState) {
        is HomeShortcutUiState.Loading -> LoadingScreen()
        is HomeShortcutUiState.Error -> ErrorScreen(
            message = (uiState as HomeShortcutUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        is HomeShortcutUiState.Success -> {
            val successState = uiState as HomeShortcutUiState.Success
            StatelessHomeShortcutScreen(
                searchQuery = successState.searchQuery,
                shortcuts = successState.shortcuts,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onScreenClick = onScreenClick
            )
        }
        else -> { /* Init state */ }
    }
}

@Composable
fun StatelessHomeShortcutScreen(
    searchQuery: String,
    shortcuts: List<ScreenShortcutData>,
    onSearchQueryChange: (String) -> Unit,
    onScreenClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Shortcuts",
            subtitle = "Quick access to your favorite screens."
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search shortcuts") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(shortcuts) { shortcut ->
                ShortcutCard(
                    shortcut = shortcut,
                    onClick = { onScreenClick(shortcut.route) }
                )
            }
        }
    }
}

@Composable
private fun ShortcutCard(
    shortcut: ScreenShortcutData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = shortcut.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
