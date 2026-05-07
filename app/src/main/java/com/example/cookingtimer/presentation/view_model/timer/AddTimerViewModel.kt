package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTimerViewModel @Inject constructor() : ViewModel() {

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

            if (name.isBlank() || duration.isBlank() || category.isBlank() || difficulty.isBlank()) {
                _uiState.value = AddTimerUiState.Error("Please fill in all fields.")
                return@launch
            }
            
            if (duration.toIntOrNull() == null) {
                _uiState.value = AddTimerUiState.Error("Duration must be numeric.")
                return@launch
            }
            
            // Success logic
            _uiState.value = AddTimerUiState.Success
            _navigationEvent.send(AddTimerNavigationEvent.NavigateBack)
        }
    }

    fun resetUiState() {
        _uiState.value = AddTimerUiState.Init
    }
}
