package com.example.cookingtimer.presentation.view_model.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.CategoryRepository
import com.example.cookingtimer.model.repository.ProfileRepository
import com.example.cookingtimer.model.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val categoryRepository: CategoryRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Init)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            profileRepository.getProfileDataFlow().collect { profile ->
                val currentState = _uiState.value
                if (currentState is DashboardUiState.Success) {
                    _uiState.value = currentState.copy(userName = profile?.displayName ?: "User")
                }
            }
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            
            val timersResult = timerRepository.getTimers()
            val categoriesResult = categoryRepository.getCategories()
            val profileResult = profileRepository.getProfileData()

            if (timersResult.isSuccess && categoriesResult.isSuccess && profileResult.isSuccess) {
                val timers = timersResult.getOrDefault(emptyList())
                val categories = categoriesResult.getOrDefault(emptyList()).map { it.name }
                val profile = profileResult.getOrNull()
                
                _uiState.value = DashboardUiState.Success(
                    userName = profile?.displayName ?: "User",
                    searchQuery = "",
                    timers = timers,
                    categories = listOf("All") + categories
                )
            } else {
                _uiState.value = DashboardUiState.Error("Failed to load dashboard data")
            }
        }
    }

    fun onSearchChange(query: String) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            _uiState.value = currentState.copy(
                searchQuery = query
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

    fun deleteTimer(timerId: Int) {
        viewModelScope.launch {
            timerRepository.deleteTimer(timerId).onSuccess {
                loadDashboard()
            }
        }
    }

    fun resetUiState() {
        loadDashboard()
    }
}
