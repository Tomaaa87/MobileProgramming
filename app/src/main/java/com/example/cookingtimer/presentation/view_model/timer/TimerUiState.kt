package com.example.cookingtimer.presentation.view_model.timer

sealed interface TimerUiState {
    object Init : TimerUiState
    object Loading : TimerUiState
    object Success : TimerUiState
    data class Error(val message: String) : TimerUiState
}
