package com.example.cookingtimer.di.repository

import com.example.cookingtimer.model.TimerData

interface TimerRepository {
    suspend fun getTimers(): Result<List<TimerData>>
    suspend fun addTimer(name: String, duration: String, category: String, difficulty: String, isFavorite: Boolean): Result<Unit>
}
