package com.example.cookingtimer.presentation.view_model.auth.registration

sealed interface RegistrationNavigationEvent {
    object Navigate : RegistrationNavigationEvent
    object NavigateBack : RegistrationNavigationEvent
}
