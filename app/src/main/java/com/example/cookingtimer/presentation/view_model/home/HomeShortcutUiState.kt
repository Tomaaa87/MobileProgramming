package com.example.cookingtimer.presentation.view_model.home

import com.example.cookingtimer.model.ScreenShortcutData

sealed interface HomeShortcutUiState {
    object Init : HomeShortcutUiState
    object Loading : HomeShortcutUiState
    data class Success(
        val searchQuery: String,
        val shortcuts: List<ScreenShortcutData>
    ) : HomeShortcutUiState
    data class Error(val message: String) : HomeShortcutUiState
}
