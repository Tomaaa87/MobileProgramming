package com.example.cookingtimer.presentation.view_model.profile

sealed interface ProfileUiState {
    object Init : ProfileUiState
    object Loading : ProfileUiState
    data class Success(val profileData: ProfileData) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
