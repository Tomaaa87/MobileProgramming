package com.example.cookingtimer.presentation.view_model.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.category.CategoryRepository
import com.example.cookingtimer.model.repository.profile.ProfileRepository
import com.example.cookingtimer.model.repository.timer.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private var timerJob: Job? = null

    init {
        // Trigger initial data load/seed
        viewModelScope.launch {
            timerRepository.getTimers()
            categoryRepository.getCategories()
        }
        observeData()
        startTicker()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                timerRepository.getTimersFlow(),
                categoryRepository.getCategoriesFlow(),
                profileRepository.getProfileDataFlow()
            ) { timers, categories, profile ->
                val categoryNames = categories.map { it.name }
                
                DashboardUiState.Success(
                    userName = profile?.displayName ?: "User",
                    searchQuery = (_uiState.value as? DashboardUiState.Success)?.searchQuery ?: "",
                    selectedCategory = (_uiState.value as? DashboardUiState.Success)?.selectedCategory ?: "All",
                    timers = timers,
                    categories = listOf("All") + categoryNames
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun startTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _uiState.value
                if (currentState is DashboardUiState.Success) {
                    currentState.timers.forEach { timer ->
                        if (timer.isRunning && timer.remainingSeconds > 0) {
                            val updatedTimer = timer.copy(
                                remainingSeconds = timer.remainingSeconds - 1,
                                isRunning = timer.remainingSeconds > 1
                            )
                            timerRepository.updateTimer(updatedTimer)
                        }
                    }
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            _uiState.value = currentState.copy(searchQuery = query)
        }
    }

    fun onCategorySelect(category: String) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            _uiState.value = currentState.copy(selectedCategory = category)
        }
    }

    fun toggleTimer(timerId: String) {
        val currentState = _uiState.value
        if (currentState is DashboardUiState.Success) {
            val timer = currentState.timers.find { it.id == timerId }
            if (timer != null) {
                val updatedTimer = timer.copy(isRunning = !timer.isRunning)
                viewModelScope.launch {
                    timerRepository.updateTimer(updatedTimer)
                }
            }
        }
    }

    fun deleteTimer(timerId: String) {
        viewModelScope.launch {
            timerRepository.deleteTimer(timerId)
        }
    }

    fun resetUiState() {
        // With Flows, we don't need manual reload, but we can trigger seeding if empty
        viewModelScope.launch {
            timerRepository.getTimers()
            categoryRepository.getCategories()
        }
    }
}
