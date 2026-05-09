package com.example.cookingtimer.di.repository

import com.example.cookingtimer.model.TimerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor() : TimerRepository {
    private val timers = mutableListOf(
        TimerData(1, "Pasta", "10:00", "Boil water, add pasta."),
        TimerData(2, "Eggs", "07:00", "Soft boiled."),
        TimerData(3, "Rice", "18:00", "Simmer, lid on."),
        TimerData(4, "Cookies", "12:00", "Bake and watch."),
        TimerData(5, "Steak", "06:00", "Medium rare."),
        TimerData(6, "Tea", "03:00", "English breakfast."),
        TimerData(7, "Soup", "45:00", "Low heat simmer."),
        TimerData(8, "Pizza", "15:00", "Crispy crust.")
    )

    override suspend fun getTimers(): Result<List<TimerData>> = withContext(Dispatchers.IO) {
        delay(1000)
        Result.success(timers.toList())
    }

    override suspend fun addTimer(
        name: String,
        duration: String,
        category: String,
        difficulty: String,
        isFavorite: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        delay(1000)
        timers.add(TimerData(timers.size + 1, name, duration, "Category: $category, Difficulty: $difficulty", false))
        Result.success(Unit)
    }
}
