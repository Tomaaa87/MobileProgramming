package com.example.cookingtimer.presentation.view_model.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.di.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : ViewModel() {

    private val allCategories = listOf(
        "All", "Breakfast", "Lunch", "Dinner", "Dessert",
        "Snacking", "Healthy", "Vegan", "Italian", "Baking"
    )

    private val shortcuts = listOf("Add Timer", "Profile", "Settings", "Achievements")

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Init)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            timerRepository.getTimers()
                .onSuccess { timers ->
                    _uiState.value = DashboardUiState.Success(
                        searchQuery = "",
                        timers = timers,
                        categories = allCategories,
                        shortcuts = shortcuts
                    )
                }
                .onFailure { error ->
                    _uiState.value = DashboardUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun onSearchChange(query: String) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            val filteredCategories = allCategories.filter { it.contains(query, ignoreCase = true) }
            _uiState.value = currentState.copy(
                searchQuery = query,
                categories = filteredCategories
            )
        }
    }

    fun toggleTimer(timerId: Int) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            val updatedTimers = currentState.timers.map {
                if (it.id == timerId) it.copy(isRunning = !it.isRunning) else it
            }
            _uiState.value = currentState.copy(timers = updatedTimers)
        }
    }

    fun resetUiState() {
        loadDashboard()
    }
}
