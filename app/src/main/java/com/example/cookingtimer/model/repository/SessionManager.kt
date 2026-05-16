package com.example.cookingtimer.model.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId

    fun login(userId: Int) {
        _currentUserId.value = userId
    }

    fun logout() {
        _currentUserId.value = null
    }
    
    fun getUserId(): Int = _currentUserId.value ?: 1
}
