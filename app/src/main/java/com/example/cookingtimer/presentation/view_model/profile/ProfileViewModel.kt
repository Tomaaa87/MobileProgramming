package com.example.cookingtimer.presentation.view_model.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookingtimer.model.repository.profile.ProfileRepository
import com.example.cookingtimer.model.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<ProfileNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                profileRepository.getProfileDataFlow().collect { data ->
                    if (data != null) {
                        _uiState.value = ProfileUiState.Success(data)
                    } else {
                        // Profile missing from Firestore, show a default instead of hanging
                        _uiState.value = ProfileUiState.Success(
                            ProfileData(
                                displayName = "New Chef",
                                email = "user@example.com",
                                bio = "Cooking enthusiast",
                                notificationsEnabled = true,
                                emailUpdates = false,
                                selectedTheme = "System",
                                fontSize = 16f,
                                achievements = emptyList()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun resetUiState() {
        _uiState.value = ProfileUiState.Init
    }

    fun updateProfile(data: ProfileData) {
        viewModelScope.launch {
            profileRepository.updateProfileData(data)
        }
    }
}
