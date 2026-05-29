package com.example.cookingtimer.model.repository.timer

import com.example.cookingtimer.model.data.local.dao.IngredientDao
import com.example.cookingtimer.model.data.local.dao.RecipeStepDao
import com.example.cookingtimer.model.data.local.dao.TimerDao
import com.example.cookingtimer.model.data.local.entity.TimerEntity
import com.example.cookingtimer.model.data.local.entity.TimerWithSteps
import com.example.cookingtimer.model.mapper.Mapper.toDomain
import com.example.cookingtimer.model.mapper.Mapper.toEntity
import com.example.cookingtimer.model.repository.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
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
                    TimerEntity(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        title = "Pasta",
                        totalSeconds = 600,
                        remainingSeconds = 600,
                        description = "Category: Italian"
                    ),
                    TimerEntity(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        title = "Soft Boiled Eggs",
                        totalSeconds = 420,
                        remainingSeconds = 420,
                        description = "Category: Breakfast"
                    )
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
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val userId = sessionManager.getUserId()
            val timerId = UUID.randomUUID().toString()
            val entity = TimerEntity(
                id = timerId,
                userId = userId,
                title = name,
                totalSeconds = totalSeconds,
                remainingSeconds = totalSeconds,
                description = "Category: $category",
                isRunning = false
            )
            timerDao.insertTimer(entity)
            Result.success(timerId)
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

    override suspend fun deleteTimer(timerId: String): Result<Unit> = withContext(Dispatchers.IO) {
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

    override suspend fun getTimerById(timerId: String): Result<TimerData?> =
        withContext(Dispatchers.IO) {
            try {
                val entity = timerDao.getTimerById(timerId)
                Result.success(entity?.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getTimersWithSteps(): Result<List<TimerWithSteps>> =
        withContext(Dispatchers.IO) {
            try {
                val userId = sessionManager.getUserId()
                val list = timerDao.getTimersWithSteps(userId).first()
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
