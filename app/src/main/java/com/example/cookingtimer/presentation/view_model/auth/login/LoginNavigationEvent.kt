package com.example.cookingtimer.presentation.view_model.auth.login

sealed interface LoginNavigationEvent {
    object Navigate : LoginNavigationEvent
    object NavigateBack : LoginNavigationEvent
}
