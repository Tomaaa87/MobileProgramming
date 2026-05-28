package com.example.cookingtimer.presentation.view_model.timer

sealed interface AddTimerNavigationEvent {
    data class NavigateToDetail(val timerId: String) : AddTimerNavigationEvent
    object NavigateBack : AddTimerNavigationEvent
}
