package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.RecipeRepository
import com.example.cookingtimer.model.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun loadTimerDetail(timerId: Int) {
        viewModelScope.launch {
            _uiState.value = TimerDetailUiState.Loading
            
            val timerResult = timerRepository.getTimerById(timerId)
            val stepsResult = recipeRepository.getSteps(timerId)
            val ingredientsResult = recipeRepository.getIngredients(timerId)

            if (timerResult.isSuccess && stepsResult.isSuccess && ingredientsResult.isSuccess) {
                val timer = timerResult.getOrNull()
                if (timer != null) {
                    _uiState.value = TimerDetailUiState.Success(
                        timer = timer,
                        steps = stepsResult.getOrDefault(emptyList()),
                        ingredients = ingredientsResult.getOrDefault(emptyList())
                    )
                } else {
                    _uiState.value = TimerDetailUiState.Error("Timer not found")
                }
            } else {
                _uiState.value = TimerDetailUiState.Error("Failed to load timer details")
            }
        }
    }

    fun toggleTimer(timerId: Int) {
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
