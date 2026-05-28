package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.recipe.RecipeRepository
import com.example.cookingtimer.model.repository.timer.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerDetailViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TimerDetailUiState>(TimerDetailUiState.Init)
    val uiState: StateFlow<TimerDetailUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null

    fun loadTimerDetail(timerId: String) {
        viewModelScope.launch {
            _uiState.value = TimerDetailUiState.Loading
            
            try {
                // Observe the timer flow for live updates from DB
                timerRepository.getTimersFlow().collect { timers ->
                    val timer = timers.find { it.id == timerId }
                    
                    if (timer != null) {
                        // Load steps and ingredients once or update reactively if needed
                        val steps = recipeRepository.getSteps(timerId).getOrDefault(emptyList())
                        val ingredients = recipeRepository.getIngredients(timerId).getOrDefault(emptyList())

                        _uiState.value = TimerDetailUiState.Success(
                            timer = timer,
                            steps = steps,
                            ingredients = ingredients
                        )
                    } else {
                        _uiState.value = TimerDetailUiState.Error("Timer not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = TimerDetailUiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
        startTicker()
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _uiState.value
                if (state is TimerDetailUiState.Success && state.timer.isRunning && state.timer.remainingSeconds > 0) {
                    val updatedTimer = state.timer.copy(
                        remainingSeconds = state.timer.remainingSeconds - 1,
                        isRunning = state.timer.remainingSeconds > 1
                    )
                    timerRepository.updateTimer(updatedTimer)
                }
            }
        }
    }

    fun toggleTimer(timerId: String) {
        val currentState = _uiState.value
        if (currentState is TimerDetailUiState.Success) {
            val updatedTimer = currentState.timer.copy(isRunning = !currentState.timer.isRunning)
            viewModelScope.launch {
                timerRepository.updateTimer(updatedTimer)
                _uiState.value = currentState.copy(timer = updatedTimer)
            }
        }
    }
}
