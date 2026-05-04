package com.example.cookingtimer.presentation.view_model.timer

sealed interface AddTimerNavigationEvent {
    object Navigate : AddTimerNavigationEvent
    object NavigateBack : AddTimerNavigationEvent
}
