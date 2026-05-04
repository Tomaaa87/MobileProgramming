package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.TimerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<TimerUiState>(TimerUiState.Init)
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val _timers = MutableStateFlow<List<TimerData>>(emptyList())
    val timers: StateFlow<List<TimerData>> = _timers.asStateFlow()

    private val _navigationEvent = Channel<TimerNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        _timers.value = listOf(
            TimerData(1, "Boil Eggs", "7 min", "Soft boiled eggs"),
            TimerData(2, "Pasta", "10 min", "Al dente pasta"),
            TimerData(3, "Steak", "6 min", "Medium rare steak")
        )
    }

    fun toggleTimer(timerId: Int, isRunning: Boolean) {
        viewModelScope.launch {
            _uiState.value = TimerUiState.Loading
            _timers.value = _timers.value.map {
                if (it.id == timerId) it.copy(isRunning = isRunning) else it
            }
            _uiState.value = TimerUiState.Success
        }
    }

    fun deleteTimer(timerId: Int) {
        viewModelScope.launch {
            _uiState.value = TimerUiState.Loading
            _timers.value = _timers.value.filter { it.id != timerId }
            _uiState.value = TimerUiState.Success
        }
    }

    fun resetUiState() {
        _uiState.value = TimerUiState.Init
    }
}
