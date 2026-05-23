package com.example.cookingtimer.presentation.view_model.timer

sealed interface AddTimerUiState {
    data class Init(val categories: List<String> = emptyList()) : AddTimerUiState
    object Loading : AddTimerUiState
    object Success : AddTimerUiState
    data class Error(val message: String) : AddTimerUiState
}
