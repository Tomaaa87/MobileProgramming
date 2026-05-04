package com.example.cookingtimer.presentation.view_model.timer

sealed interface AddTimerUiState {
    object Init : AddTimerUiState
    object Loading : AddTimerUiState
    object Success : AddTimerUiState
    data class Error(val message: String) : AddTimerUiState
}
