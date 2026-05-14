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
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerDao: TimerDao,
    private val recipeStepDao: RecipeStepDao,
    private val ingredientDao: IngredientDao
) : TimerRepository {

    override suspend fun getTimers(): Result<List<TimerData>> = withContext(Dispatchers.IO) {
        try {
            var entities = timerDao.getAllTimers().first()
            if (entities.isEmpty()) {
                val seed = listOf(
                    TimerEntity(title = "Pasta", duration = "10:00", description = "Category: Italian"),
                    TimerEntity(title = "Soft Boiled Eggs", duration = "07:00", description = "Category: Breakfast"),
                    TimerEntity(title = "White Rice", duration = "18:00", description = "Category: Healthy"),
                    TimerEntity(title = "Steak", duration = "06:00", description = "Category: Dinner")
                )
                seed.forEach { timerDao.insertTimer(it) }
                entities = timerDao.getAllTimers().first()
            }
            Result.success(entities.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTimer(
        name: String,
        duration: String,
        category: String,
        isFavorite: Boolean,
        ingredients: List<Pair<String, String>>,
        steps: List<String>
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val entity = TimerEntity(
                title = name,
                duration = duration,
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
            timerDao.updateTimer(timer.toEntity())
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
            val list = timerDao.getTimersWithSteps().first()
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
