package com.example.cookingtimer.presentation.view_model.home

sealed interface HomeShortcutNavigationEvent {
    object Navigate : HomeShortcutNavigationEvent
    object NavigateBack : HomeShortcutNavigationEvent
}
