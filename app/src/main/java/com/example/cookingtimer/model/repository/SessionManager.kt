package com.example.cookingtimer.model.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    fun login(userId: String) {
        _currentUserId.value = userId
    }

    fun logout() {
        _currentUserId.value = null
    }
    
    fun getUserId(): String = _currentUserId.value ?: ""
}
