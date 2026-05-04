package com.example.cookingtimer.presentation.view_model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.ScreenShortcutData
import com.example.cookingtimer.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeShortcutViewModel @Inject constructor() : ViewModel() {

    private val allShortcuts = listOf(
        ScreenShortcutData("My Timers", Screen.Timers.route),
        ScreenShortcutData("Achievements", Screen.Achievements.route),
        ScreenShortcutData("Add Quest", Screen.AddTimer.route),
        ScreenShortcutData("Habits", Screen.Habits.route),
        ScreenShortcutData("Quests", Screen.Quests.route),
        ScreenShortcutData("Profile", Screen.Profile.route),
        ScreenShortcutData("Dashboard", Screen.Dashboard.route)
    )

    private val _uiState = MutableStateFlow<HomeShortcutUiState>(HomeShortcutUiState.Init)
    val uiState: StateFlow<HomeShortcutUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<HomeShortcutNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadShortcuts()
    }

    private fun loadShortcuts() {
        viewModelScope.launch {
            _uiState.value = HomeShortcutUiState.Loading
            _uiState.value = HomeShortcutUiState.Success(
                searchQuery = "",
                shortcuts = allShortcuts
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        val filteredShortcuts = allShortcuts.filter {
            it.title.contains(query, ignoreCase = true)
        }
        _uiState.value = HomeShortcutUiState.Success(
            searchQuery = query,
            shortcuts = filteredShortcuts
        )
    }

    fun resetUiState() {
        loadShortcuts()
    }
}
