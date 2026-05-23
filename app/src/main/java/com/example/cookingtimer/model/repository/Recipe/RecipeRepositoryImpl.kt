package com.example.cookingtimer.model.repository.recipe

import com.example.cookingtimer.model.data.local.dao.IngredientDao
import com.example.cookingtimer.model.data.local.dao.RecipeStepDao
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val ingredientDao: IngredientDao,
    private val recipeStepDao: RecipeStepDao
) : RecipeRepository {

    override suspend fun getIngredients(timerId: Int): Result<List<IngredientEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val list = ingredientDao.getIngredientsForTimer(timerId).first()
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getSteps(timerId: Int): Result<List<RecipeStepEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val list = recipeStepDao.getStepsForTimer(timerId).first()
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun addIngredient(timerId: Int, name: String, amount: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                ingredientDao.insertIngredient(
                    IngredientEntity(
                        timerId = timerId,
                        name = name,
                        amount = amount
                    )
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun addStep(timerId: Int, instruction: String, stepNumber: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                recipeStepDao.insertStep(
                    RecipeStepEntity(
                        timerId = timerId,
                        instruction = instruction,
                        stepNumber = stepNumber
                    )
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}