package com.example.cookingtimer.presentation.view_model.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.TimerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val allTimers = listOf(
        TimerData(1, "Pasta", "10:00", "Boil water, add pasta."),
        TimerData(2, "Eggs", "07:00", "Soft boiled."),
        TimerData(3, "Rice", "18:00", "Simmer, lid on."),
        TimerData(4, "Cookies", "12:00", "Bake and watch."),
        TimerData(5, "Steak", "06:00", "Medium rare."),
        TimerData(6, "Tea", "03:00", "English breakfast."),
        TimerData(7, "Soup", "45:00", "Low heat simmer."),
        TimerData(8, "Pizza", "15:00", "Crispy crust.")
    )

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
            _uiState.value = DashboardUiState.Success(
                searchQuery = "",
                timers = allTimers,
                categories = allCategories,
                shortcuts = shortcuts
            )
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
