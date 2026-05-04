package com.example.cookingtimer.presentation.view_model.timer

sealed interface TimerNavigationEvent {
    object Navigate : TimerNavigationEvent
    object NavigateBack : TimerNavigationEvent
}
