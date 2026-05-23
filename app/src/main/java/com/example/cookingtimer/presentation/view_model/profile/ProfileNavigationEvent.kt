package com.example.cookingtimer.presentation.view_model.profile

sealed interface ProfileNavigationEvent {
    object Navigate : ProfileNavigationEvent
    object NavigateBack : ProfileNavigationEvent
}
