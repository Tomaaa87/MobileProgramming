package com.example.cookingtimer.presentation.view_model.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.CategoryRepository
import com.example.cookingtimer.model.repository.TimerRepository
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
    private val timerRepository: TimerRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddTimerUiState>(AddTimerUiState.Init())
    val uiState: StateFlow<AddTimerUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<AddTimerNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().onSuccess { categories ->
                _uiState.value = AddTimerUiState.Init(categories.map { it.name })
            }
        }
    }

    fun addTimer(
        name: String,
        duration: String,
        category: String,
        isFavorite: Boolean,
        ingredients: List<Pair<String, String>> = emptyList(),
        steps: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            _uiState.value = AddTimerUiState.Loading

            val totalSeconds = (duration.toLongOrNull() ?: 0L) * 60L
            timerRepository.addTimer(name, totalSeconds, category, isFavorite, ingredients, steps)
                .onSuccess { timerId ->
                    _uiState.value = AddTimerUiState.Success
                    _navigationEvent.send(AddTimerNavigationEvent.NavigateToDetail(timerId.toInt()))
                }
                .onFailure { error ->
                    _uiState.value = AddTimerUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun resetUiState() {
        _uiState.value = AddTimerUiState.Init()
    }
}
