package com.example.cookingtimer.presentation.view_model.dashboard

import com.example.cookingtimer.model.repository.timer.TimerData

sealed interface DashboardUiState {
    object Init : DashboardUiState
    object Loading : DashboardUiState
    data class Success(
        val userName: String,
        val searchQuery: String,
        val selectedCategory: String = "All",
        val timers: List<TimerData>,
        val categories: List<String>
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
