package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.TimerData
import com.example.cookingtimer.model.data.local.entity.CategoryEntity
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity
import com.example.cookingtimer.model.data.local.entity.TimerWithSteps

import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun getTimersFlow(): Flow<List<TimerData>>
    suspend fun getTimers(): Result<List<TimerData>>
    suspend fun addTimer(
        name: String,
        totalSeconds: Long,
        category: String,
        isFavorite: Boolean,
        ingredients: List<Pair<String, String>> = emptyList(),
        steps: List<String> = emptyList()
    ): Result<Long>
    suspend fun updateTimer(timer: TimerData): Result<Unit>
    suspend fun deleteTimer(timerId: Int): Result<Unit>
    suspend fun getTimerById(timerId: Int): Result<TimerData?>
    suspend fun getTimersWithSteps(): Result<List<TimerWithSteps>>
}
