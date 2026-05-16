package com.example.cookingtimer.model.repository

import com.example.cookingtimer.model.TimerData
import com.example.cookingtimer.model.data.local.dao.IngredientDao
import com.example.cookingtimer.model.data.local.dao.RecipeStepDao
import com.example.cookingtimer.model.data.local.dao.TimerDao
import com.example.cookingtimer.model.data.local.entity.*
import com.example.cookingtimer.model.data.local.util.Mapper.toDomain
import com.example.cookingtimer.model.data.local.util.Mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerDao: TimerDao,
    private val recipeStepDao: RecipeStepDao,
    private val ingredientDao: IngredientDao,
    private val sessionManager: SessionManager
) : TimerRepository {

    override fun getTimersFlow(): Flow<List<TimerData>> {
        val userId = sessionManager.getUserId()
        return timerDao.getAllTimers(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTimers(): Result<List<TimerData>> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            var entities = timerDao.getAllTimers(userId).first()
            if (entities.isEmpty()) {
                val seed = listOf(
                    TimerEntity(userId = userId, title = "Pasta", totalSeconds = 600, remainingSeconds = 600, description = "Category: Italian"),
                    TimerEntity(userId = userId, title = "Soft Boiled Eggs", totalSeconds = 420, remainingSeconds = 420, description = "Category: Breakfast"),
                    TimerEntity(userId = userId, title = "White Rice", totalSeconds = 1080, remainingSeconds = 1080, description = "Category: Healthy"),
                    TimerEntity(userId = userId, title = "Steak", totalSeconds = 360, remainingSeconds = 360, description = "Category: Dinner")
                )
                seed.forEach { timerDao.insertTimer(it) }
                entities = timerDao.getAllTimers(userId).first()
            }
            Result.success(entities.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTimer(
        name: String,
        totalSeconds: Long,
        category: String,
        isFavorite: Boolean,
        ingredients: List<Pair<String, String>>,
        steps: List<String>
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            val entity = TimerEntity(
                userId = userId,
                title = name,
                totalSeconds = totalSeconds,
                remainingSeconds = totalSeconds,
                description = "Category: $category",
                isRunning = false
            )
            val timerId = timerDao.insertTimer(entity).toInt()

            ingredients.forEach { (name, amount) ->
                ingredientDao.insertIngredient(
                    IngredientEntity(timerId = timerId, name = name, amount = amount)
                )
            }

            steps.forEachIndexed { index, instruction ->
                recipeStepDao.insertStep(
                    RecipeStepEntity(timerId = timerId, stepNumber = index + 1, instruction = instruction)
                )
            }

            Result.success(timerId.toLong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTimer(timer: TimerData): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            timerDao.updateTimer(timer.toEntity(userId = userId))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTimer(timerId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val entity = timerDao.getTimerById(timerId)
            if (entity != null) {
                timerDao.deleteTimer(entity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Timer not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTimerById(timerId: Int): Result<TimerData?> = withContext(Dispatchers.IO) {
        try {
            val entity = timerDao.getTimerById(timerId)
            Result.success(entity?.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTimersWithSteps(): Result<List<TimerWithSteps>> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            val list = timerDao.getTimersWithSteps(userId).first()
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
