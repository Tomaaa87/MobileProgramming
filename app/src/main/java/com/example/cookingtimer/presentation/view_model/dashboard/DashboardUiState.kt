package com.example.cookingtimer.presentation.view_model.dashboard

import com.example.cookingtimer.model.TimerData

sealed interface DashboardUiState {
    object Init : DashboardUiState
    object Loading : DashboardUiState
    data class Success(
        val searchQuery: String,
        val timers: List<TimerData>,
        val categories: List<String>,
        val shortcuts: List<String>
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
