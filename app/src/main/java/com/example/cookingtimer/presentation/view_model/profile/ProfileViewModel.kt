package com.example.cookingtimer.presentation.view_model.profile

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
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<ProfileNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            val profileData = ProfileData(
                displayName = "Tom Smith",
                bio = "Home cook and pasta lover.",
                notificationsEnabled = true,
                emailUpdates = false,
                selectedTheme = "System",
                fontSize = 16f,
                achievements = listOf("Timer Pro", "Egg Master", "Pasta King", "Chef de Cuisine", "Quick Learner")
            )
            
            _uiState.value = ProfileUiState.Success(profileData)
        }
    }

    fun resetUiState() {
        _uiState.value = ProfileUiState.Init
    }
}
