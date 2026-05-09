package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.di.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddTimerUiState>(AddTimerUiState.Init)
    val uiState: StateFlow<AddTimerUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<AddTimerNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun addTimer(
        name: String,
        duration: String,
        category: String,
        difficulty: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = AddTimerUiState.Loading

            timerRepository.addTimer(name, duration, category, difficulty, isFavorite)
                .onSuccess {
                    _uiState.value = AddTimerUiState.Success
                    _navigationEvent.send(AddTimerNavigationEvent.NavigateBack)
                }
                .onFailure { error ->
                    _uiState.value = AddTimerUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun resetUiState() {
        _uiState.value = AddTimerUiState.Init
    }
}
