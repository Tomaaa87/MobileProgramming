package com.example.cookingtimer.presentation.view_model.timer

sealed interface AddTimerNavigationEvent {
    data class NavigateToDetail(val timerId: Int) : AddTimerNavigationEvent
    object NavigateBack : AddTimerNavigationEvent
}
